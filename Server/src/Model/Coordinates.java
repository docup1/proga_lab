package Model;

import java.io.Serializable;

/**
 * Класс, представляющий координаты.
 */
public class Coordinates implements Serializable {
    private long x;
    private double y;

    /**
     * Конструктор для инициализации координат.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Coordinates(long x, double y) {
        this.x = x;
        this.y = y;
    }
    public Coordinates(){}
    public void setX(long x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    /**
     * Возвращает координату X.
     *
     * @return значение координаты X
     */
    public long getX() {
        return x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return значение координаты Y
     */
    public double getY() {
        return y;
    }

    /**
     * Переопределение метода toString для вывода информации о координатах.
     *
     * @return строковое представление объекта Coordinates
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
