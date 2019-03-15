package ws.slink.mine.model;

import ws.slink.mine.type.Crypto;

/**
 *
 * Wallet information query result
 *
 */
public class WalletInfo {
    public Crypto crypto;
    public String wallet;
    public double amount;

    public WalletInfo(Crypto crypto, String wallet, double amount) {
        this.crypto = crypto;
        this.wallet = wallet;
        this.amount = amount;
    }

    public String toString() {
        return "c: " + crypto + "; w: " + wallet + "; a: " + amount;
    }
}
