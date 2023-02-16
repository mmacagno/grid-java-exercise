package exceptions;

import java.io.IOException;

public final class InvalidValueException extends IOException {
    public InvalidValueException(int value) {
        super(String.format("The provided value does not meet the requirements: %d", value));
    }
}
