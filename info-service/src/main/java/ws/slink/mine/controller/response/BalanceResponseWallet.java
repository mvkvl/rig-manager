package ws.slink.mine.controller.response;

import ws.slink.mine.model.BalanceData;
import ws.slink.mine.model.Crypto;

public class BalanceResponseWallet implements DataResponseBalance {

    protected Crypto crypto;
    protected String source = "wallet";
    protected double mining;
    protected double holding;

    @Override public Crypto getCrypto() {
        return crypto;
    }
    @Override public String getSource() {
        return source;
    }
    @Override public void setCrypto(Crypto value) {
        this.crypto = value;
    }
    @Override public void setSource(String value) {
        this.source = "wallet";
    }

    @Override public void setAmount(BalanceData data) {
        if ("mining".equalsIgnoreCase(data.type)) {
            this.mining = data.amount;
        } else {
            this.holding = data.amount;
        }
    }

    public double getMining()  {
        return this.mining;
    }
    public double getHolding() {
        return this.holding;
    }

    public String toString() {
        return this.getCrypto() + "." + this.getSource() + ": " + this.getHolding() + "; " + this.getMining();
    }


    public static void main(String [] args) {
        BalanceData bdm = new BalanceData();
        bdm.source = "wallet";
        bdm.type   = "mining";
        bdm.crypto = Crypto.RVN;
        bdm.amount = 100;

        BalanceData bdh = new BalanceData();
        bdh.source = "wallet";
        bdh.type   = "holding";
        bdh.crypto = Crypto.RVN;
        bdh.amount = 500;

        DataResponseBalance wbr = DataResponseBalance.valueOf(bdm);
        wbr.add(bdh);
        System.out.println(wbr);
    }

}
