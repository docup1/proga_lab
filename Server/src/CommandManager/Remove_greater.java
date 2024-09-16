package CommandManager;

import CollectionManager.CollectionManager;
import Managers.UserStatusManager;
import Model.Vehicle;
import Response.*;

import java.util.Iterator;
import java.util.logging.Logger;

import static CollectionManager.IDManager.GetNewId;
import static CollectionManager.IDManager.ListID;

/**
 * Remove_greater - удаляет из коллекции все элементы, превышающие заданный
 */
public class Remove_greater extends Command{
    private final CollectionManager collectionManager;
    private final Logger logger;
    public Remove_greater(CollectionManager collectionManager, CommandManager commandManager, Logger logger){
        super("remove_greater", " удалить из коллекции все элементы, превышающие заданный");
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
        if (!userStatusManager.getStatus()){
            return new Response(STATUS.OK, "Войдите в аккаунт!");
        }
        if ((args == null || args.isEmpty())) {
            if (object.equals("")){
                return new Response(STATUS.NEED_OBJECT, "* Создание нового Vehicle:", GetNewId());
            } else {
                Vehicle a = (Vehicle) object;
                if (a.validate()) {
                    var ins = collectionManager.getCollection().size();
                    Iterator<Vehicle> iterator = collectionManager.getCollection().iterator();
                    while (iterator.hasNext()) {
                        Vehicle b = iterator.next();
                        if (b.compareTo(a) > 0 && b.getUserName().equals(userStatusManager.getUserName())) {
                            ListID.remove((int) b.getId());
                            iterator.remove();
                        }
                    }
                    var removedCount = ins - collectionManager.getCollection().size();
                    logger.info(super.getName());
                    return new Response(STATUS.OK, "Успешно удалено " + (removedCount) + " элементов");
                } else {
                    logger.warning("Поля vehicle не валидны! Vehicle не создан!");
                    return new Response(STATUS.ERROR, "Поля vehicle не валидны! Vehicle не создан!");
                }
            }
        }
        else{
            logger.warning("Неправильное количество аргументов!)");
            return new Response(STATUS.ERROR,
                    "Неправильное количество аргументов!)");
        }
    }
}
