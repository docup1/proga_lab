package Model;

import java.io.Serializable;

/**
 * Перечисление, представляющее типы топлива.
 */
public enum FuelType implements Serializable {
    ELECTRICITY,
    MANPOWER,
    PLASMA;

    /**
     * Возвращает строку с названиями всех типов топлива.
     * @return строка с названиями типов топлива
     */
    public static String names() {
        StringBuilder builder = new StringBuilder();
        FuelType[] types = FuelType.values();
        for (int i = 0; i < types.length; i++) {
            builder.append(types[i].name());
            if (i < types.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
