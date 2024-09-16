package CommandManager;
import Managers.UserStatusManager;
import Model.Vehicle;
import Response.*;
import CollectionManager.CollectionManager;
import Response.Response;
import java.util.logging.Logger;
import static CollectionManager.IDManager.GetNewId;

/**
 * Add - добавляет элемент в коллекцию
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    private final Logger logger;
    public Add(CollectionManager collectionManager, CommandManager commandManager, Logger logger) {
        super("add", " добавить новый элемент в коллекцию");
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
        if (!userStatusManager.getStatus()){
            return new Response(STATUS.OK, "Войдите в аккаунт!");
        }
        if ((args == null || args.isEmpty())) {
            if (object.equals("")){
                logger.info(userStatusManager.getUserName() + " -> " + "Отправка запроса на создание объекта");
                return new Response(STATUS.NEED_OBJECT, "* Создание нового Vehicle:", GetNewId());
            } else {
                Vehicle a = (Vehicle) object;
                if (a.validate()) {
                    a.setUserName(userStatusManager.getUserName());
                    collectionManager.add(a);
                    logger.info(userStatusManager.getUserName() + " -> " + super.getName());
                    return new Response(STATUS.OK, "Vehicle успешно добавлен!");
                } else
                    return new Response(STATUS.ERROR, "Поля vehicle не валидны! Vehicle не создан!");
            }
        }
        else{
            logger.warning(userStatusManager.getUserName() + " -> " + "Неправильное количество аргументов!)");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!)");
        }
    }
}
