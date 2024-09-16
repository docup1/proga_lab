package Model;

import java.io.Serializable;

/**
 * Перечисление типов транспортных средств.
 */
public enum VehicleType implements Serializable {
    HELICOPTER,
    DRONE,
    MOTORCYCLE,
    HOVERBOARD,
    SPACESHIP;

    /**
     * Возвращает строку с именами всех типов транспортных средств.
     * @return строка с именами типов транспортных средств
     */
    public static String names() {
        StringBuilder builder = new StringBuilder();
        VehicleType[] types = VehicleType.values();
        for (int i = 0; i < types.length; i++) {
            builder.append(types[i].name());
            if (i < types.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
