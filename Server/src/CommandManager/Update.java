package CommandManager;
import Managers.UserStatusManager;
import Model.Vehicle;
import Response.*;
import CollectionManager.CollectionManager;
import Response.Response;

import java.util.logging.Logger;

/**
 * Команда Update - обновляет значение элемента коллекции, id которого равен заданному.
 */
public class Update extends Command{
    private final CollectionManager collectionManager;
    private final Logger logger;
    public Update(CollectionManager collectionManager, CommandManager commandManager, Logger logger) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному");
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
        if (args == null || args.isEmpty()) {
            logger.warning(userStatusManager.getUserName() + " -> " + "Неправильное количество аргументов!");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!");
        } else {
            int id = Integer.parseInt(args.split(" ")[0]);
            boolean exist = false;
            for (Vehicle vehicle : collectionManager.getCollection()) {
                if (vehicle.getId() == id && vehicle.getUserName().equals(userStatusManager.getUserName())) {
                    exist = true;
                    break;
                }
            }
            if (exist){
                if (object.equals("")){
                    logger.info(userStatusManager.getUserName() + " -> " + "Отправка запроса на создание объекта");
                    return new Response(STATUS.NEED_OBJECT, "* Создание нового Vehicle:", id);
                } else {
                    if (collectionManager.getCollection().isEmpty()) {
                        System.out.println(super.getName());
                        logger.info(super.getName());
                        return new Response(STATUS.OK, "Коллекция пустая");
                    }
                }
                Vehicle a = (Vehicle) object;
                if (a.validate()) {
                    collectionManager.remove(id);
                    collectionManager.add(a);
                    a.setUserName(userStatusManager.getUserName());
                    logger.info(super.getName());
                    return new Response(STATUS.OK, "Vehicle успешно обновлен!");
                } else {
                    logger.warning(userStatusManager.getUserName() + " -> " + "Поля vehicle не валидны! Vehicle не создан!");
                    return new Response(STATUS.ERROR, "Поля vehicle не валидны! Vehicle не создан!");
                }
            } else {
                logger.info(userStatusManager.getUserName() + " -> " + "На вашем аккаунте не существует такого ID");
                return new Response(STATUS.ERROR, "На вашем аккаунте не существует такого ID");
            }
        }
    }
}
