package ws.slink.mine.blockchain.config;

import ws.slink.mine.type.Crypto;

/**
 *
 *  Proce configuration
 *  ("price.*" in application.yml )
 *
 */
public class PriceConfig {

    private String url;
    private Crypto crypto;
    private String currencies;

    private String getUrlKey() {
        return ("api.price.url");
    }

    private String getKey(Crypto crypto, String key) {
        StringBuilder sb = new StringBuilder("api.network.");
        sb.append(crypto.toString());
        sb.append(".");
        sb.append(key);
        return sb.toString();
    }

}
