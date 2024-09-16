package Model;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс, представляющий транспортное средство.
 */
public class Vehicle extends Element implements Validatable, Serializable {
    private long id;
    private String name;
    private Coordinates coordinates;
    private java.time.LocalDateTime creationDate;
    private double enginePower;
    private int numberOfWheels;
    private VehicleType type;
    private FuelType fuelType;
    private String userName;
    /**
     * Конструктор для создания объекта транспортного средства.
     * @param id уникальный идентификатор
     * @param name название транспортного средства
     * @param coordinates координаты транспортного средства
     * @param creationDate дата создания
     * @param enginePower мощность двигателя
     * @param numberOfWheels количество колес
     * @param type тип транспортного средства
     * @param fuelType тип топлива
     */
    public Vehicle(long id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate, double enginePower, int numberOfWheels, VehicleType type, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.numberOfWheels = numberOfWheels;
        this.type = type;
        this.fuelType = fuelType;
    }
    public Vehicle(){}
    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getEnginePower() {
        return enginePower;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    public VehicleType getType() {
        return type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setId(int freeId) {
        this.id = freeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEnginePower(double enginePower) {
        this.enginePower = enginePower;
    }

    public void setNumberOfWheels(int numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public void update(Vehicle vehicle) {
        if (vehicle != null) {
            if (vehicle.getName() != null) {
                this.name = vehicle.getName();
            }
            if (vehicle.getCoordinates() != null) {
                this.coordinates = vehicle.getCoordinates();
            }
            if (vehicle.getCreationDate() != null) {
                this.creationDate = vehicle.getCreationDate();
            }
            if (vehicle.getEnginePower() > 0) {
                this.enginePower = vehicle.getEnginePower();
            }
            if (vehicle.getNumberOfWheels() > 0) {
                this.numberOfWheels = vehicle.getNumberOfWheels();
            }
            if (vehicle.getType() != null) {
                this.type = vehicle.getType();
            }
            if (vehicle.getFuelType() != null) {
                this.fuelType = vehicle.getFuelType();
            }
        }
    }

    /**
     * Проверяет, является ли объект транспортного средства допустимым.
     * @return true, если объект допустим, в противном случае - false
     */
    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (creationDate == null) return false;
        if (coordinates == null) return false;
        if (enginePower <= 0) return false;
        if (numberOfWheels <= 0) return false;
        if (type == null) return false;
        return true;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int compareTo(Element element) {
        return (int)(this.id - element.getId());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "user=" + userName +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", enginePower=" + enginePower +
                ", numberOfWheels=" + numberOfWheels +
                ", type=" + type +
                ", fuelType=" + fuelType +
                '}';
    }
}

