package Managers;
import Model.Coordinates;
import Model.Vehicle;
import Server.Server;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.sql.*;
import java.util.logging.Logger;
import static CollectionManager.IDManager.AddId;
import static Managers.PasswordManager.hashPassword;
public class DumpManager {
    private Connection connection;
    public TreeSet<Vehicle> readFromDataBase(){
        PreparedStatement preparedStatement;
        TreeSet<Vehicle> vehicles = new TreeSet<>();
        try {
            String sql = "SELECT * FROM Vehicle";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                Coordinates coordinates = new Coordinates();
                vehicle.setId(rs.getLong("id"));
                AddId((int)rs.getLong("id"));
                vehicle.setName(rs.getString("name"));
                coordinates.setX(rs.getLong("coordinates_x"));
                coordinates.setY(rs.getFloat("coordinates_y"));
                vehicle.setCoordinates(coordinates);
                vehicle.setCreationDate(rs.getTimestamp("creationDate").toLocalDateTime());
                vehicle.setNumberOfWheels(rs.getInt("numberOfWheels"));
                vehicle.setEnginePower(rs.getDouble("enginePower"));
                vehicle.setType(CastManager.castToVehicleType(rs.getString("vType")));
                vehicle.setFuelType(CastManager.castToFuelType(rs.getString("fuelType")));
                vehicle.setUserName(rs.getString("userName"));
                vehicles.add(vehicle);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.warning("Error while reading from Vehicle");
        }
        return vehicles;
    }
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public DumpManager(Properties info, String propPath) throws IOException {
        System.setProperty("java.util.logging.config.file", propPath);
        start(info);
    }
    public void start(Properties info) throws IOException {
        while (true){
            try {
                connection = DriverManager.getConnection(Files.readString(Paths.get(System.getenv("url"))), info);
                logger.info("Successfully connected to the database");
                break;
            } catch (SQLException | NullPointerException e) {
                logger.warning("Error with connection to DataBase");
                System.out.println("Enter Y if you want to stop server");
                Scanner scanner = new Scanner(System.in);
                if (scanner.nextLine().equalsIgnoreCase("Y")){
                    logger.info("Stop working...");
                    System.exit(0);
                    break;
                }
            }
        }
    }
    public void initDataBase(){
        createUserTable();
        createTableVehicle();
    }

    public void createTableCoordinates(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = """
                    CREATE TABLE IF NOT EXISTS Coordinates (
                    \tx bigint NOT NULL,
                    \ty float NOT NULL,
                    \tPRIMARY KEY (x, y)\s
                    );""";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            logger.warning("Error creating COORDINATES");
        }
    }
    public void createTableVehicle(){
        createTableCoordinates();
        createIdSeq();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = """
                    CREATE TABLE IF NOT EXISTS Vehicle (
                    \tid bigint NOT NULL DEFAULT nextval('ID_SEQ'),
                    \tname TEXT NOT NULL,
                    \tcoordinates_x bigint NOT NULL,
                    \tcoordinates_y float NOT NULL,
                    \tFOREIGN KEY(coordinates_x, coordinates_y) REFERENCES Coordinates(x, y),
                    \tcreationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    \tenginePower float CHECK (enginePower > 0),
                    \tnumberOfWheels int NOT NULL CHECK (numberOfWheels > 0),
                    \tvType TEXT NOT NULL,
                    \tfuelType TEXT NOT NULL,
                    \tuserName TEXT NOT NULL,
                    FOREIGN KEY (userName) REFERENCES users(userName));""";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            logger.warning("Error creating Vehicle");
        }
    }

    public void createUserTable(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS USERS " +
                    "(userName TEXT PRIMARY KEY, " +
                    " password TEXT);";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            logger.warning("Error creating USERS");
        }
    }

    public void createIdSeq(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE SEQUENCE IF NOT EXISTS ID_SEQ START WITH 1 INCREMENT BY 1;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            logger.warning("Error creating SEQUENCE");
        }
    }

    public boolean checkUser(String user_name){
        boolean exists = false;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        try {
            String sql = "SELECT COUNT(*) AS count FROM users WHERE userName = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user_name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > 0) {
                    exists = true;
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.warning("Error while checking user");
        }
        return exists;
    }

    public void registerUser(String userName, String pswd){
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO users (userName, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, pswd);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Error adding user registration");
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.warning("Error with closing statement");
            }
        }
    }
    public boolean checkPassword(String userName, String pswd) {
        String sql = "SELECT password FROM Users WHERE userName = ?";
        PreparedStatement prepareStatement;
        try {
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, userName);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                String hashedInputPassword = hashPassword(pswd);
                prepareStatement.close();
                resultSet.close();
                return hashedInputPassword.equals(hashedPassword);
            }
            prepareStatement.close();
            resultSet.close();
        } catch (SQLException ex) {
            logger.warning("Error while reading userName and passwd");
        }
        return false;
    }
    public ArrayList<String> getUsers() {
        String sql = "SELECT userName FROM Users;";
        PreparedStatement prepareStatement;
        ArrayList<String> users = new ArrayList<>();
        try {
            prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                String user = resultSet.getString("userName");
                users.add(user);
            }
            if (!users.isEmpty()){
                prepareStatement.close();
                resultSet.close();
            } else {
                users.add("There are no users yet...");
                return users;
            }
        } catch (SQLException ex) {
            logger.warning("Error while reading FROM users");
        }
        return users;
    }
    public void saveToDataBase(TreeSet<Vehicle> vehicles, String userName){
        StringBuilder sql = new StringBuilder("DELETE FROM vehicle WHERE userName = ?;");
        PreparedStatement prepareStatement;
        for (Vehicle vehicle: vehicles){
            if (vehicle.getUserName().equals(userName)){
                if (!vehicle.getUserName().isEmpty()){
                    var value = getValue(vehicle);
                    sql.append(value);
                }
            }
        }
        try {
            prepareStatement = connection.prepareStatement(sql.toString());
            prepareStatement.setString(1, userName);
            prepareStatement.executeQuery();
            prepareStatement.close();
        } catch (SQLException ex) {
            logger.info("Saved");
        }
    }
    private static String getValue(Vehicle vehicle) {
        String value = "INSERT INTO Coordinates(x, y)" +
                "VALUES (" + vehicle.getCoordinates().getX() + "," + vehicle.getCoordinates().getY()+ ") ON CONFLICT (x, y) DO NOTHING;";
        value += "INSERT INTO Vehicle(id, name, coordinates_x, coordinates_y, creationDate, enginePower, numberOfWheels, vType, fuelType, userName)" +
                "VALUES";
        value += "(" + vehicle.getId() + "," + "'" + vehicle.getName() + "'" + "," + vehicle.getCoordinates().getX() + "," + vehicle.getCoordinates().getY()
                + "," + "'" + vehicle.getCreationDate() + "'" +  "," + vehicle.getEnginePower() + "," + vehicle.getNumberOfWheels() + "," + "'" + vehicle.getType() + "'" + ","
                + "'" + vehicle.getFuelType() + "'" + "," + "'" + vehicle.getUserName() + "'" +");";
        System.out.println(value);
        return value;
    }
}


