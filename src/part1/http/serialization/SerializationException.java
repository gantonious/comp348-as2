package part1.http.serialization;

/**
 * Created by George on 2017-12-26.
 */
public class SerializationException extends RuntimeException {
    private Exception innerException;

    public SerializationException(Exception innerException) {
        super("Failed to serialize data.");
        this.innerException = innerException;
    }

    public Exception getInnerException() {
        return innerException;
    }
}
