package ws.slink.mine.model;

import ws.slink.mine.type.Crypto;

public class BalanceData {

    public Crypto crypto; //
    public String source; // pool | wallet
    public String type;   // confirmed | unconfirmed | mining | holding
    public double amount; //

    public BalanceData crypto(Crypto value) {
        this.crypto = value;
        return this;
    }
    public BalanceData source(String value) {
        this.source = value;
        return this;
    }
    public BalanceData type(String value) {
        this.type = value;
        return this;
    }
    public BalanceData amount(double value) {
        this.amount = value;
        return this;
    }
    public String toString() {
        return crypto + "." + source + "." + type + ": " + amount;
    }

}
