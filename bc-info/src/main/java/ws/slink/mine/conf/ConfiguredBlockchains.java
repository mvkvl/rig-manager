package ws.slink.mine.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.tools.ConfigTools;

import java.util.*;

/**
 *
 * get list of configured blockchains from application.yml
 *
 */
@Component
public class ConfiguredBlockchains {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguredBlockchains.class);

    @Autowired
    Environment env;

    Set<Crypto> cryptoSet;

    public Collection<Crypto> currencies() {
        if (null == cryptoSet || cryptoSet.isEmpty())
            initCryptoSet();
        return cryptoSet;
    }

    private void initCryptoSet() {
        cryptoSet = new HashSet<>();
        ConfigTools.getPropertiesByPrefix(env, "api.network").keySet()
            .stream()
            .forEach(k -> {
                try {
                    String[] arr = k.replace("api.network.", "").split("\\.");
                    cryptoSet.add(Crypto.valueOf(arr[0].toUpperCase()));
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
            }
        );
    }

}
