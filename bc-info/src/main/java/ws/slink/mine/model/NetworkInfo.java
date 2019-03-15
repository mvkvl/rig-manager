package ws.slink.mine.model;

import ws.slink.mine.type.Crypto;

/**
 *
 * Blockchain network information query result
 *
 */
public class NetworkInfo {
    public Crypto crypto;
    public double diff;
    public double hashrate;

    public NetworkInfo(Crypto crypto, double diff, double hr) {
        this.crypto = crypto;
        this.diff = diff;
        this.hashrate = hr / 1000000.0;  // express hashrate in MH
    }

    public NetworkInfo(Crypto crypto, String diff, String hr) {
        this.crypto = crypto;
        this.diff = Double.parseDouble(diff);
        this.hashrate = Double.parseDouble(hr) / 1000000.0; // express hashrate in MH
    }

    public String toString() {
        return "c: " + crypto + "; d: " + diff + "; h: " + hashrate;
    }


}
