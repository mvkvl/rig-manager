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
 * get list of configured wallets from application.yml
 *
 */
@Component
public class ConfiguredWallets {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguredWallets.class);

    @Autowired
    Environment env;

    Map<Crypto, List<String>> walletMap;

    public List<Crypto> currencies() {
        if (null == walletMap || walletMap.isEmpty())
            initWalletMap();
        return new ArrayList(walletMap.keySet());
    }

    public Collection<String> wallets(Crypto crypto) {
        if (null == walletMap || walletMap.isEmpty())
            initWalletMap();
        if (walletMap.containsKey(crypto))
            return walletMap.get(crypto);
        else
            return Collections.EMPTY_LIST;
    }

    private void initWalletMap() {
        walletMap = new HashMap<>();
        ConfigTools.getPropertiesByPrefix(env, "wallet").keySet()
            .stream()
            .forEach(k -> {
                try {
                    String[] arr = k.replace("wallet.", "").split("\\.");
                    Crypto key = Crypto.valueOf(arr[0].toUpperCase());
                    if (walletMap.containsKey(key))
                        walletMap.get(key).add(arr[1]);
                    else
                        walletMap.put(key, new ArrayList(Arrays.asList(arr[1])));
                    } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
            }
        );
    }


}
