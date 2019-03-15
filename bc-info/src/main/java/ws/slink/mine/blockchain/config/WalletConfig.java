package ws.slink.mine.blockchain.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.slink.mine.error.WalletAPIConfigurationError;
import ws.slink.mine.tools.ConfigParams;
import ws.slink.mine.type.Crypto;

/**
 *
 *  Wallet configuration
 *  ("wallet.*" in application.yml )
 *
 */
public class WalletConfig {

    private static final Logger logger = LoggerFactory.getLogger(WalletConfig.class);

    String url;

    private String getUrlKey(Crypto crypto) {
        StringBuilder sb = new StringBuilder("api.wallet.");
        sb.append(crypto.toString());
        sb.append(".url");
        return sb.toString();
    }

    public WalletConfig(ConfigParams config, Crypto crypto) {
        this.url = config.get(getUrlKey(crypto));
        if (StringUtils.isEmpty(url))
            throw new WalletAPIConfigurationError("wallet api not configured for " + crypto);
    }

    public String url(String wallet) {
        return url.replace("%WALLET%", wallet);
    }
    public String url() {
        return url;
    }
}
