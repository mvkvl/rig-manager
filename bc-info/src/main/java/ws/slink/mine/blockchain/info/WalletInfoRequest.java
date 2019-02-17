package ws.slink.mine.blockchain.info;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.blockchain.config.WalletConfig;
import ws.slink.mine.conf.ConfigParams;
import ws.slink.mine.model.WalletInfo;
import ws.slink.mine.tools.FluentJson;
import ws.slink.mine.tools.RequestTools;

import java.util.Optional;

/**
 *
 *  Wallet balance information requester
 *
 */
@Component
public class WalletInfoRequest {

    private static final Logger logger = LoggerFactory.getLogger(WalletInfoRequest.class);

    @Autowired
    private ConfigParams config;

    @Autowired
    private RestTemplate restTemplate;

    private String getWalletKey(Crypto crypto, String type) {
        StringBuilder sb = new StringBuilder("wallet.");
        sb.append(crypto.toString());
        sb.append(".");
        sb.append(type);
        return sb.toString();
    }

    public Optional<WalletInfo> balance(Crypto crypto, String walletKey) {
        WalletConfig wc = new WalletConfig(config, crypto);
        String url = wc.url(config.get(getWalletKey(crypto, walletKey)));
//        logger.trace("URL: " + url);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, RequestTools.getEntity(), String.class);
        logger.trace("RESPONSE: [" + res.getStatusCode() + "]:  " + res.getBody());

        if (res.getStatusCode().is2xxSuccessful()) {
            try {
                FluentJson fj = new FluentJson(new JSONParser().parse(res.getBody()));
                return Optional.of(
                        new WalletInfo(
                                crypto,
                                walletKey,
                                Double.parseDouble(String.valueOf(fj.get("balance")))));
            } catch (ParseException e) {
                logger.error("Error parsing service response in WalletInfoRequest.balance()");
                return Optional.empty();
            }
        } else {
            logger.error("Service request failed in WalletInfoRequest.balance()");
            return Optional.empty();
        }
    }

}
