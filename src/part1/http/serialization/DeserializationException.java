package part1.http.serialization;

/**
 * Created by George on 2017-12-26.
 */
public class DeserializationException extends RuntimeException {
    private Exception innerException;

    public DeserializationException(Exception innerException) {
        super("Failed to deserialize data.");
        this.innerException = innerException;
    }

    public Exception getInnerException() {
        return innerException;
    }
}
