package exceptions;

import java.io.IOException;

public final class InvalidFormatException extends IOException {
    public InvalidFormatException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return String.format("File does not have the expected format: %s", super.getMessage());
    }
}
