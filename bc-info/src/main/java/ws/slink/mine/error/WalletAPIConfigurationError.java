package ws.slink.mine.error;

public class WalletAPIConfigurationError extends RuntimeException {

    public WalletAPIConfigurationError() {
        super();
    }

    public WalletAPIConfigurationError(String message) {
        super(message);
    }
}
