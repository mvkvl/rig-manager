package ws.slink.mine.error;

public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException() {
        super();
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }

}
