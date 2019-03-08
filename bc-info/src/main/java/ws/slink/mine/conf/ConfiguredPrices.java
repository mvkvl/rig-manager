package ws.slink.mine.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.tools.ConfigTools;

import java.util.*;

@Component
public class ConfiguredPrices {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguredPrices.class);

    @Autowired
    Environment env;

    Map<Crypto, List<String>> pricesMap;

    public List<Crypto> cryptos() {
        if (null == pricesMap || pricesMap.isEmpty())
            initPricesMap();
        return new ArrayList(pricesMap.keySet());
    }
    public Collection<String> currencies(Crypto crypto) {
        if (null == pricesMap || pricesMap.isEmpty())
            initPricesMap();
        if (pricesMap.containsKey(crypto))
            return pricesMap.get(crypto);
        else
            return Collections.EMPTY_LIST;
    }

    private void initPricesMap() {
        pricesMap = new HashMap<>();
        ConfigTools.getPropertiesByPrefix(env, "price").keySet()
                .stream()
                .forEach(k -> {
                            try {
                                Crypto key = Crypto.valueOf(k.replace("price.", "").toUpperCase());
                                if (pricesMap.containsKey(key))
                                    pricesMap.get(key).add(env.getProperty(k));
                                else
                                    pricesMap.put(key, new ArrayList(Arrays.asList(env.getProperty(k))));
                            } catch (Exception ex) {
                                logger.error(ex.getMessage());
                            }
                        }
                );
    }

}
