package CommandManager;

import CollectionManager.CollectionManager;
import Managers.UserStatusManager;
import Model.Vehicle;
import Response.*;
import java.util.List;
import java.util.logging.Logger;
/**
 * Команда 'filter_by_number_of_wheels'. Выводит элементы, значение поля numberOfWheels которых равно заданному.
 */
public class FilterByNumberOfWheelsServer extends Command{
    private final CollectionManager collectionManager;
    private final Logger logger;
    public FilterByNumberOfWheelsServer(CollectionManager collectionManager, CommandManager commandManager, Logger logger) {
        super("filter_by_number_of_wheels {numberOfWheels}", "вывести элементы, значение поля numberOfWheels которых равно заданному");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.logger = logger;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public Response execution(String args, Object object, UserStatusManager userStatusManager) {
        if (args == null || args.isEmpty()) {
            logger.warning("Неправильное количество аргументов!)");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!)");
        }
        else{
            int now = Integer.parseInt(args.split("")[0]);
            if (collectionManager.getCollection().isEmpty()) {
                return new Response(STATUS.OK, "Все элементы коллекции удалены");
            }
            List<Vehicle> filteredVehicles = collectionManager.getCollection().stream()
                    .filter(vehicle -> vehicle.getNumberOfWheels() == now).toList();
            return new Response(STATUS.OK, "Элементы коллекции", filteredVehicles.toString());
        }
    }
}
