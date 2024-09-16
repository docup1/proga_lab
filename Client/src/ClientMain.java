import Utility.PortAsker;
public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost", PortAsker.getPort());
        client.run();
    }
}
