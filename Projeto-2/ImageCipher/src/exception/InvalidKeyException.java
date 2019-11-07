package exception;

/**
 * Created by thales on 13/05/17.
 */
public class InvalidKeyException extends Exception {
    public InvalidKeyException() {
    }

    public InvalidKeyException(String message) {
        super(message);
    }
}
