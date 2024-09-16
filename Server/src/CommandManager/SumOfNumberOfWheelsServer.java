package CommandManager;
import CollectionManager.CollectionManager;
import Managers.UserStatusManager;
import Model.Vehicle;
import Response.*;

import java.util.logging.Logger;

/**
 * Команда 'sum_of_number_of_wheels'. Выводит сумму значений поля numberOfWheels для всех элементов коллекции.
 */
public class SumOfNumberOfWheelsServer extends Command {
    private final CollectionManager collectionManager;
    private final Logger logger;
    public SumOfNumberOfWheelsServer(CollectionManager collectionManager, CommandManager commandManager, Logger logger){
        super("sum_of_number_of_wheels", "вывести сумму значений поля numberOfWheels для всех элементов коллекции\"");
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
        if ((args == null || args.isEmpty())){
            logger.info(super.getName());
            if (!collectionManager.getCollection().isEmpty()) {
                double sum = collectionManager.getCollection().stream()
                        .mapToDouble(Vehicle::getNumberOfWheels)
                        .sum();
                System.out.println(super.getName());
                return new Response(STATUS.OK, "Сумма колёс всех элементов коллекции = " + sum);
            }
            else {System.out.println(super.getName());
                return new Response(STATUS.OK, "Коллекция пустая(");}
        }
        else {
            logger.warning("Неправильное количество аргументов!)");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!)");
        }
    }
}
