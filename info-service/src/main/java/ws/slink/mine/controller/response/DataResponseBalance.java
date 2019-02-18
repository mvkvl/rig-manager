package ws.slink.mine.controller.response;

import ws.slink.mine.model.BalanceData;
import ws.slink.mine.model.Crypto;

public interface DataResponseBalance {

    Crypto getCrypto();
    String getSource();
    void setAmount(BalanceData data);

    void setCrypto(Crypto value);
    void setSource(String value);

    static DataResponseBalance valueOf(BalanceData data) {
        if ("wallet".equalsIgnoreCase(data.source)) {
            BalanceResponseWallet wbr = new BalanceResponseWallet();
            wbr.setCrypto(data.crypto);
            wbr.setSource(data.source);
            wbr.setAmount(data);
            return wbr;
        } else {
            BalanceResponsePool pbr = new BalanceResponsePool();
            pbr.setCrypto(data.crypto);
            pbr.setSource(data.source);
            pbr.setAmount(data);
            return pbr;
        }
    }

    default boolean add(BalanceData data) {
        if (this.getCrypto() == data.crypto
         && this.getSource().equals(data.source)) {
            this.setAmount(data);
            return true;
        } else {
            return false;
        }
    }

}
