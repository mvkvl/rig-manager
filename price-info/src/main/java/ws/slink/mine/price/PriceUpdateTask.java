package ws.slink.mine.price;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ws.slink.mine.type.Crypto;
import ws.slink.mine.model.PriceInfo;

import java.util.Optional;
import java.util.function.Supplier;

public class PriceUpdateTask implements Supplier<Optional<PriceInfo>> {

    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateTask.class);

    @Autowired
    private RestTemplateBuilder restTemplate;
    @Value("${price.url}")
    private String serviceURLStrTemplate;
    @Value("${price.key:}")
    private String serviceApiKey;

    private Crypto crypto;
    private String currencies;

    public PriceUpdateTask(Crypto crypto, String currencies) {
        logger.trace("created task: [" + crypto + "]");
        this.crypto = crypto;
        this.currencies = currencies;
    }

    @Override
    public Optional<PriceInfo> get() {
        logger.trace("URL: " + serviceURLStrTemplate);
        PriceInfo pi = new PriceInfo(crypto);
        pi.prices(getJsonData(crypto, currencies));
        if (pi.getPrices().isEmpty())
            return Optional.empty();
        else
            return Optional.of(pi);
    }

    private String getJsonData(Crypto crypto, String currencies) {
        String urlStr = serviceURLStrTemplate
                .replace("%CRYPTO%", crypto.toString().toUpperCase())
                .replace("%CURRENCIES%", currencies.toUpperCase())
                ;

        if (StringUtils.isNotBlank(serviceApiKey))
            urlStr += "&api_key=" + serviceApiKey;

        String res = restTemplate.build().getForObject(urlStr, String.class);
        return res;
    }


}
