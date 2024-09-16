package CollectionManager;
import Managers.DumpManager;
import Model.Coordinates;
import Model.FuelType;
import Model.Vehicle;
import Model.VehicleType;;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

import static CollectionManager.IDManager.ListID;

public class CollectionManager{
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;
    private TreeSet<Vehicle> collection = new TreeSet<>();
    public CollectionManager(DumpManager dumpManager){
        this.dumpManager = dumpManager;
    }
    public Vehicle VehicleCreator(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, double enginePower, int numberOfWheels, VehicleType type, FuelType fuelType) {
        return new Vehicle(id, name, coordinates, creationDate, enginePower, numberOfWheels, type, fuelType);
    }
    public void setCollection(TreeSet<Vehicle> vehicles){
        this.collection = vehicles;
        setLastInitTime(LocalDateTime.now());
    }
    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastInitTime(LocalDateTime lastInitTime) {
        this.lastInitTime = lastInitTime;
    }

    public void setLastSaveTime(LocalDateTime lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }
    /**
     * @return коллекция.
     */
    public TreeSet<Vehicle> getCollection() {
        return collection;
    }


    /**
     * Получает продукт по его ID.
     * @param id ID продукта.
     * @return Продукт с указанным ID или null, если такой не найден.
     */
    public Vehicle getById(long id) {
        return collection.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /*
     * Сохраняет коллекция в указанный файл
     */
    /**
     * Сохраняет коллекцию в файл с помощью DumpManager.
     */
    public void saveCollection(String userName) {
        System.out.println(this.getCollection());
        dumpManager.saveToDataBase(this.getCollection(), userName);
        setLastSaveTime(LocalDateTime.now());
    }


    /**
     * Добавляет продукт в коллекцию.
     * @param product Продукт для добавления.
     */
    public void add(Vehicle product) {
        collection.add(product);
        ListID.add((int)product.getId());
    }

    /**
     * Удаляет продукт по его ID из коллекции.
     * @param id ID продукта для удаления.
     */
    public void remove(long id) {
        collection.removeIf(product -> product.getId() == id);
        ListID.remove((int) id);
    }

    public ArrayList<String> getUsers(){
        return dumpManager.getUsers();
    }
}
