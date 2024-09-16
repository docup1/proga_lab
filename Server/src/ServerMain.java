import CollectionManager.CollectionManager;
import CommandManager.*;
import Managers.DumpManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerMain {
    private final static Logger logger = Logger.getLogger(ServerMain.class.getName());
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port;
        Class.forName("org.postgresql.Driver");
        Properties info = new Properties();
        var commandManager = new CommandManager();
        info.load(new FileInputStream(System.getenv("cfg")));
        DumpManager dumpManager = new DumpManager(info, System.getenv("logprop"));
        var collectionManager = new CollectionManager(dumpManager);
        dumpManager.initDataBase();
        collectionManager.setCollection(dumpManager.readFromDataBase());
        commandManager.addCommand("show", new Show(collectionManager, commandManager, logger));
        commandManager.addCommand("help", new Help(commandManager, logger));
        commandManager.addCommand("info", new Info(collectionManager, commandManager, logger));
        commandManager.addCommand("clear", new Clear(collectionManager,  commandManager, logger));
        commandManager.addCommand("add", new Add(collectionManager, commandManager, logger));
        commandManager.addCommand("remove_by_id", new Remove_by_id(collectionManager, commandManager, logger));
        commandManager.addCommand("update", new Update(collectionManager, commandManager, logger));
        commandManager.addCommand("add_if_max", new Add_if_max(collectionManager, commandManager, logger));
        commandManager.addCommand("remove_greater", new Remove_greater(collectionManager, commandManager, logger));
        commandManager.addCommand("save", new Save(collectionManager, commandManager, logger));
        commandManager.addCommand("filter_by_number_of_wheels", new FilterByNumberOfWheelsServer(collectionManager, commandManager, logger));
        commandManager.addCommand("print_field_ascending_fuel_type", new PrintFieldAscendingFuelTypeServer(collectionManager, commandManager, logger));
        commandManager.addCommand("remove_lower", new Remove_lower(collectionManager, commandManager, logger));
        commandManager.addCommand("sum_of_number_of_wheels", new SumOfNumberOfWheelsServer(collectionManager, commandManager, logger));
        commandManager.addCommand("users", new Users(collectionManager, commandManager, logger));
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите порт: ");
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (port > 0 && port <= 65535) {
                    break;
                }
            } catch (NumberFormatException e){
                System.out.println("Попробуйте ещё раз");
            }
        }
        Server server = new Server(port, commandManager, System.getenv("logprop"), dumpManager);
        server.start();
    }
}