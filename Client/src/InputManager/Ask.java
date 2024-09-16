package InputManager;

import Model.Coordinates;
import Model.FuelType;
import Model.Vehicle;
import Model.VehicleType;

import java.util.NoSuchElementException;

/**
 * Класс Ask предоставляет методы для ввода данных о транспортном средстве с консоли.
 */
public class Ask {
    /**
     * Исключение, используемое для выхода из цикла ввода.
     */
    public static class AskBreak extends Exception {}

    /**
     * Запрашивает данные о транспортном средстве с консоли.
     * @param console консольный интерфейс
     * @param id идентификатор транспортного средства
     * @return объект транспортного средства
     * @throws AskBreak если пользователь завершает ввод
     */
    public static Vehicle askVehicle(Console console, long id) throws AskBreak {
        try {
            String name = askName(console);
            Coordinates coordinates = askCoordinates(console);
            java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();
            double enginePower = askEnginePower(console);
            int numberOfWheels = askNumberOfWheels(console);
            VehicleType type = askVehicleType(console);
            FuelType fuelType = askFuelType(console);
            return new Vehicle(id, name, coordinates, creationDate, enginePower, numberOfWheels, type, fuelType);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    private static String askName(Console console) throws AskBreak {
        try {
            String name;
            while (true) {
                console.print("name: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.isEmpty()) {
                    break;
                }
            }
            return name;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    private static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            long x;
            console.print("coordinates.x: ");
            var lineX = console.readln().trim();
            if (lineX.equals("exit")) throw new AskBreak();
            if (!lineX.equals("")) {
                try {
                    x = Long.parseLong(lineX);
                } catch (NumberFormatException e) {
                    return askCoordinates(console);
                }
            } else {
                return askCoordinates(console);
            }

            double y;
            console.print("coordinates.y: ");
            var lineY = console.readln().trim();
            if (lineY.equals("exit")) throw new AskBreak();
            if (!lineY.equals("")) {
                try {
                    y = Double.parseDouble(lineY);
                } catch (NumberFormatException e) {
                    return askCoordinates(console);
                }
            } else {
                return askCoordinates(console);
            }

            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    private static double askEnginePower(Console console) throws AskBreak {
        try {
            double enginePower;
            while (true) {
                console.print("enginePower: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        enginePower = Double.parseDouble(line);
                        if (enginePower <= 0) {
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return enginePower;
        } catch (NoSuchElementException | IllegalStateException e) {
            return 0;
        }
    }

    private static int askNumberOfWheels(Console console) throws AskBreak {
        try {
            int numberOfWheels;
            while (true) {
                console.print("numberOfWheels: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        numberOfWheels = Integer.parseInt(line);
                        if (numberOfWheels <= 0) {
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return numberOfWheels;
        } catch (NoSuchElementException | IllegalStateException e) {
            return 0;
        }
    }

    private static VehicleType askVehicleType(Console console) throws AskBreak {
        try {
            VehicleType type;
            while (true) {
                console.print("VehicleType (" + VehicleType.names() + "): ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        type = VehicleType.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                    }
                } else {
                    return null;
                }
            }
            return type;
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
    }

    private static FuelType askFuelType(Console console) throws AskBreak {
        try {
            FuelType fuelType;
            while (true) {
                console.print("FuelType (" + FuelType.names() + "): ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        fuelType = FuelType.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                    }
                } else {
                    return null;
                }
            }
            return fuelType;
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
    }
}
