package ws.slink.mine.blockchain.info;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ws.slink.mine.blockchain.config.NetworkConfig;
import ws.slink.mine.info.NetworkInfo;
import ws.slink.mine.tools.ConfigParams;
import ws.slink.mine.tools.FluentJson;
import ws.slink.mine.tools.RequestTools;
import ws.slink.mine.type.Crypto;

import java.util.Optional;

/**
 *
 *  Blockchain network information requester
 *
 */
@Component
public class NetworkInfoRequest {

    private static final Logger logger = LoggerFactory.getLogger(NetworkInfoRequest.class);

    @Autowired
    private ConfigParams config;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<NetworkInfo> get(Crypto crypto) {
        NetworkConfig nc = new NetworkConfig(config, crypto);
        logger.trace("URL: " + nc.url());
        ResponseEntity<String> res = restTemplate.exchange(nc.url(),
                                                           HttpMethod.GET,
                                                           RequestTools.getEntity(),
                                                           String.class);
        logger.trace("[" + res.getStatusCode() + "]:  " + res.getBody());

        if (res.getStatusCode().is2xxSuccessful()) {
            try {
                FluentJson json = (StringUtils.isNotBlank(nc.field())) ?
                        new FluentJson(new JSONParser().parse(res.getBody())).get(nc.field()) :
                        new FluentJson(new JSONParser().parse(res.getBody()));
                return Optional.of(
                        new NetworkInfo(
                            crypto,
                            json.get(nc.diff()).toString(),
                            json.get(nc.rate()).toString()
                        )
                );
            } catch (ParseException e) {
                logger.error("Error parsing service response in NetworkInfoRequest.get()");
                return Optional.empty();
            }
        } else {
            logger.error("Service request failed in NetworkInfoRequest.get()");
            return Optional.empty();
        }
    }
}
