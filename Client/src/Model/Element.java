package Model;
import java.io.Serializable;

/**
 * Абстрактный класс, представляющий элемент коллекции.
 * Реализует интерфейсы Comparable, Validatable, Serializable.
 */
public abstract class Element implements Comparable<Element>, Validatable, Serializable {

    /**
     * Абстрактный метод для получения идентификатора элемента.
     *
     * @return идентификатор элемента
     */
    abstract public long getId();
}

