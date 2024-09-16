package CommandManager;

import CollectionManager.CollectionManager;
import Managers.UserStatusManager;
import Response.*;

import java.util.logging.Logger;

/**
 * Команда 'print_field_ascending_fuel_type'. Выводит значения поля fuelType всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingFuelTypeServer extends Command{
    private final Logger logger;
    private final CollectionManager collectionManager;
    public PrintFieldAscendingFuelTypeServer(CollectionManager collectionManager, CommandManager commandManager, Logger logger){
        super("print_field_ascending_fuel_type", "вывести значения поля fuelType всех элементов в порядке возрастания");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.logger = logger;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public Response execution(String args, Object object, UserStatusManager userStatusManager){
        StringBuilder stringBuilder = new StringBuilder();
        if ((args == null || args.isEmpty())){
            logger.info(super.getName());
            if (!collectionManager.getCollection().isEmpty()) {
                stringBuilder.append("Отсортированные элементы коллекции ").append("\n");
                collectionManager.getCollection().stream().sorted().forEach(vehicle -> stringBuilder.append(vehicle.getFuelType()).append("\n"));
                return new Response(STATUS.OK, "Значения fuelType успешно выведены в порядке возрастания\n" + stringBuilder);
            }
            else {return new Response(STATUS.OK, "Коллекция пустая(");}
        }
        else {
            logger.info("Неправильное количество аргументов!)");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!)");
        }
    }
}
