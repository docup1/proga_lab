package Response;

import java.io.Serializable;

public enum STATUS implements Serializable {
    ERROR,
    OK,
    NEED_OBJECT,
    OBJECT,
    SAVE,
    USERCHECK,
    COMMAND;
    private static final long serialVersionUID = 228L;
}
