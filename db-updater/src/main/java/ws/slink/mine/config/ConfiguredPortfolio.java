package ws.slink.mine.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ws.slink.mine.tools.ConfigTools;
import ws.slink.mine.type.Crypto;

import java.util.*;

@Component
public class ConfiguredPortfolio {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguredPortfolio.class);

    @Autowired
    Environment env;

    Map<Crypto, List<String>> portfolio;

    public List<Crypto> cryptos() {
        if (null == portfolio || portfolio.isEmpty())
            loadPortfolio();
        return new ArrayList(portfolio.keySet());
    }
    public Collection<String> currencies(Crypto crypto) {
        if (null == portfolio || portfolio.isEmpty())
            loadPortfolio();
        if (portfolio.containsKey(crypto))
            return portfolio.get(crypto);
        else
            return Collections.EMPTY_LIST;
    }
    private void loadPortfolio() {
        portfolio = new HashMap<>();
        ConfigTools.getPropertiesByPrefix(env, "portfolio").keySet()
                .stream()
                .forEach(k -> {
                            try {
                                Crypto key = Crypto.valueOf(k.replace("portfolio.", "").toUpperCase());
                                if (portfolio.containsKey(key))
                                    portfolio.get(key).add(env.getProperty(k));
                                else
                                    portfolio.put(key, new ArrayList(Arrays.asList(env.getProperty(k).split(","))));
                            } catch (Exception ex) {
                                logger.error(ex.getMessage());
                            }
                        }
                );
    }


}
