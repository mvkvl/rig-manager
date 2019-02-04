package ws.slink.mine.model;

import java.util.Date;

/**
 *
 * Wallet information query result
 *
 */
public class WalletInfo {
    public Crypto crypto;
    public String wallet;
    public double amount;
    public Date receivedAt;

    public WalletInfo() {
        this.receivedAt = new Date();
    }

    public WalletInfo(Crypto crypto, String wallet, double amount) {
        this.crypto     = crypto;
        this.wallet     = wallet;
        this.amount     = amount;
        this.receivedAt = new Date();
    }

    public String toString() {
        return "c: " + crypto + "; w: " + wallet + "; a: " + amount + "; dt: " + receivedAt;
    }

}
