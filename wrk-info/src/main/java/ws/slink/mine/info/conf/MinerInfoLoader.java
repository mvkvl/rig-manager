package ws.slink.mine.info.conf;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.Crypto;
import ws.slink.mine.tools.FluentJson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MinerInfoLoader {

    private static final Logger logger = LoggerFactory.getLogger(MinerInfoLoader.class);

    @Value("${api.miner.urls}")
    private List<String> minerInfoUrls;

    @Autowired
    private RestTemplateBuilder restTemplate;

    /**
     * @return list of miners' configuration to query mine details later
     */
    public List<MinerInfo> get() {
//        System.out.println(" >>> " + minerInfoUrls);
        return minerInfoUrls.parallelStream()
                            .map(this::getMinersInfo)
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
    }

    /**
     *
     * Multiple miners can be running on a rig simultaneously,
     * thus we need to get configuration information for all of them
     *
     * @param urlStr
     * @return list of miners running on given rig (by rig-url:mine-ws-port)
     */
    private List<MinerInfo> getMinersInfo(String urlStr) {
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed url exception: " + urlStr); }

        String jsonStr = restTemplate.build().getForObject(urlStr, String.class);

//        if (logger.isTraceEnabled())
//            logger.trace("Miner WS: {}", jsonStr.trim());

        FluentJson fj;
        try {
            fj = new FluentJson(new JSONParser().parse(jsonStr));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON parse exception: " + jsonStr);
        }

        List<MinerInfo> result =
            fj.stream()
              .map(v -> new MinerInfo()
                                    .crypto(Crypto.valueOf(v.getString("crypto").toUpperCase()))
                                    .pool(v.getString("pool"))
                                    .worker(v.getString("worker"))
                                    .rig(v.getString("rig"))
                                    .miner(v.getString("miner"))
                                    .host(url.getHost())
                                    .port(v.getString("apiport"))
             ).filter(v -> v.configured())
              .collect(Collectors.toList());

//        if (logger.isTraceEnabled()) {
//            logger.trace("Miners Info: ");
//            result.stream().forEach(v -> logger.trace(v.toString()));
//        }

        return result;
    }

}
