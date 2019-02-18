package ws.slink.mine.controller.response;

import ws.slink.mine.model.BalanceData;
import ws.slink.mine.model.Crypto;

public class BalanceResponsePool implements DataResponseBalance {

    private Crypto crypto;
    private String source;
    private double confirmed;
    private double unconfirmed;

    @Override public Crypto getCrypto() {
        return this.crypto;
    }
    @Override public String getSource() {
        return this.source;
    }
    @Override public void setCrypto(Crypto value) {
        this.crypto = value;
    }
    @Override public void setSource(String value) {
        this.source = value;
    }

    @Override public void setAmount(BalanceData data) {
        if ("unconfirmed".equalsIgnoreCase(data.type)) {
            this.unconfirmed = data.amount;
        } else {
            this.confirmed = data.amount;
        }
    }

    public double getConfirmed()  {
        return this.confirmed;
    }
    public double getUnconfirmed() {
        return this.unconfirmed;
    }

    public String toString() {
        return this.getCrypto() + "." + this.getSource() + ": " + this.getUnconfirmed() + "; " + this.getConfirmed();
    }

}
