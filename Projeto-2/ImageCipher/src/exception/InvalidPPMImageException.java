package exception;

/**
 * Created by thales on 14/05/17.
 */
public class InvalidPPMImageException extends Exception {
    public InvalidPPMImageException() {
    }

    public InvalidPPMImageException(String message) {
        super(message);
    }
}
