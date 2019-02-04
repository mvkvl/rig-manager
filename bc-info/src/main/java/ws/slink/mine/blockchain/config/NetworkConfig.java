package ws.slink.mine.blockchain.config;

import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.conf.ConfigParams;

/**
 *
 *  Blockchain network configuration
 *  ("api.network.*" in application.yml )
 *
 */
public class NetworkConfig {

    private String url;
    private String field;
    private String diff;
    private String rate;

    private String getKey(Crypto crypto, String key) {
        StringBuilder sb = new StringBuilder("api.network.");
        sb.append(crypto.toString());
        sb.append(".");
        sb.append(key);
        return sb.toString();
    }

    public NetworkConfig(ConfigParams config, Crypto crypto) {
        this.url   = config.get(getKey(crypto, "url"));
        this.field = config.get(getKey(crypto, "field"));
        this.diff  = config.get(getKey(crypto, "diff"));
        this.rate  = config.get(getKey(crypto, "nethash"));
    }

    public String url() {
        return url;
    }

    public String field() {
        return field;
    }

    public String diff() {
        return diff;
    }

    public String rate() {
        return rate;
    }
}
