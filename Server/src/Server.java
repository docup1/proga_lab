import CommandManager.CommandManager;
import Launcher.LaunchCommand;
import Managers.DumpManager;
import Managers.PasswordManager;
import Managers.UserStatusManager;
import Response.Response;
import Response.STATUS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int port;
    private final LaunchCommand launchCommand;
    private final Map<SocketChannel, ObjectOutputStream> clients;
    private final Map<SocketChannel, UserStatusManager> users;
    private final ForkJoinPool requestThreadPool;
    private final ForkJoinPool responseThreadPool;
    private final DumpManager dumpManager;

    public Server(int port, CommandManager commandManager, String propPath, DumpManager dumpManager) {
        this.port = port;
        this.launchCommand = new LaunchCommand(commandManager);
        this.dumpManager = dumpManager;
        clients = Collections.synchronizedMap(new HashMap<>());
        users = Collections.synchronizedMap(new HashMap<>());
        requestThreadPool = new ForkJoinPool();
        responseThreadPool = new ForkJoinPool();
        System.setProperty("java.util.logging.config.file", propPath);
    }

    public void start() throws IOException {
        logger.info("Starting the server");
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        while (true) {
            logger.info("Waiting for client connection");
            SocketChannel clientChannel = serverSocketChannel.accept();
            synchronized (clients) {
                ObjectOutputStream oos = new ObjectOutputStream(clientChannel.socket().getOutputStream());
                UserStatusManager userStatusManager = new UserStatusManager(false, "");
                clients.put(clientChannel, oos);
                users.put(clientChannel, userStatusManager);
            }
            requestThreadPool.execute(() -> handleClient(clientChannel));
        }
    }

    private void handleClient(SocketChannel clientChannel) {
        try {
            UserStatusManager userStatusManager = users.get(clientChannel);
            ObjectInputStream ois = new ObjectInputStream(clientChannel.socket().getInputStream());
            while (true) {
                try {
                    Response message = (Response) ois.readObject();
                    logger.info("Received a request from client: " + userStatusManager.getUserName());
                    if (message.getStatus().equals(STATUS.COMMAND)) {
                        if (message.getMessage().equals("save")) {
                            sendResponse(clientChannel, new Response(STATUS.ERROR, "Такой команды не существует \nЧтобы сохраниться выйдите из аккаунта!"));
                        } else {
                            new Thread(() -> {
                                Response commandResult = launchCommand.commandParser(message.getMessage(), message.getObject(), userStatusManager);
                                sendResponse(clientChannel, commandResult);
                            }).start();
                        }
                    } else if (message.getStatus().equals(STATUS.USERCHECK)) {
                        new Thread(() -> handleUserCheck(clientChannel, message, userStatusManager)).start();
                    } else {
                        Response commandResult = launchCommand.doCommand("save", "", "", userStatusManager);
                        sendResponse(clientChannel, commandResult);
                    }
                } catch (IOException e) {
                    logger.info("Client = " + userStatusManager.getUserName() + " - disconnected");
                    synchronized (clients) {
                        clients.remove(clientChannel);
                        userStatusManager.setUserName("");
                        userStatusManager.setStatus(false);
                        users.remove(clientChannel);
                    }
                    break;
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "Error reading object from client = " + userStatusManager.getUserName());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error handling client: = " + users.get(clientChannel).getUserName());
        }
    }

    private void handleUserCheck(SocketChannel clientChannel, Response message, UserStatusManager userStatusManager) {
        try {
            Response commandResult;
            switch (message.getMessage()) {
                case "checkUser":
                    commandResult = new Response(STATUS.USERCHECK, "", dumpManager.checkUser((String) message.getObject()));
                    sendResponse(clientChannel, commandResult);
                    break;
                case "registerUser":
                    String data = (String) message.getObject();
                    dumpManager.registerUser(data.split(" ")[0], PasswordManager.hashPassword(data.split(" ")[1]));
                    logger.info("User = " + userStatusManager.getUserName() + " added successfully!");
                    commandResult = new Response(STATUS.USERCHECK, "User added successfully!");
                    userStatusManager.setUserName(data.split(" ")[0]);
                    userStatusManager.setStatus(true);
                    sendResponse(clientChannel, commandResult);
                    break;
                case "checkPassword":
                    data = (String) message.getObject();
                    boolean passwordMatch = dumpManager.checkPassword(data.split(" ")[0], data.split(" ")[1]);
                    commandResult = new Response(STATUS.USERCHECK, "", passwordMatch);
                    if (passwordMatch) {
                        userStatusManager.setUserName(data.split(" ")[0]);
                        userStatusManager.setStatus(true);
                        logger.info("User = " + userStatusManager.getUserName() + " successfully logged in!");
                    } else {
                        logger.info("Passwords are different");
                    }
                    sendResponse(clientChannel, commandResult);
                    break;
                case "logout":
                    logger.info("logout...");
                    userStatusManager.setUserName("");
                    userStatusManager.setStatus(false);
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error handling user check: ", e);
        }
    }

    private void sendResponse(SocketChannel clientChannel, Response response) {
        responseThreadPool.execute(() -> {
            synchronized (clients) {
                ObjectOutputStream oos = clients.get(clientChannel);
                try {
                    logger.info("Sending response to client: " + users.get(clientChannel).getUserName());
                    oos.writeObject(response);
                    oos.flush();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error sending response to client: " + users.get(clientChannel).getUserName(), e);
                }
            }
        });
    }
}

