package ws.slink.mine.info.api.pool;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.PoolInfo;
import ws.slink.mine.model.Crypto;
import ws.slink.mine.tools.FluentJson;

import java.util.Arrays;
import java.util.List;

public class SuprnovaPoolAPI implements PoolAPI {


//    private final String STATUS_URL  = "https://%CRYPTO%.suprnova.cc/index.php?page=api&action=getuserstatus&api_key=%KEY%";
    private final String BALANCE_URL = "https://%CRYPTO%.suprnova.cc/index.php?page=api&action=getuserbalance&api_key=%KEY%";
    private final String WORKER_URL  = "https://%CRYPTO%.suprnova.cc/index.php?page=api&action=getuserworkers&api_key=%KEY%";

    private static final Logger logger = LoggerFactory.getLogger(SuprnovaPoolAPI.class);

    @Value("${api.pool.key.suprnova}")
    private String apiKey;

    @Autowired
    private RestTemplateBuilder restTemplate;

    @Override
    public PoolInfo get(MinerInfo minerInfo) {
        logger.trace("SuprnovaPoolAPI.get(" + minerInfo + ")");

        FluentJson balanceJson = getJson(getServiceData(BALANCE_URL, minerInfo.crypto), "getuserbalance.commands");
        FluentJson workersJson = getJson(getServiceData(WORKER_URL , minerInfo.crypto), "getuserworkers.commands");

        double workerHashrate =
            workersJson.stream()
                       .filter(item -> item.getString("username").toLowerCase().contains(minerInfo.worker.toLowerCase()))
                       .map(item -> item.getDouble("hashrate"))
                       .findFirst().orElseGet(() -> Double.valueOf(0));

        return new PoolInfo()
                             .crypto(minerInfo.crypto)
                             .pool(minerInfo.pool)
                             .worker(minerInfo.worker)
                             .hashrate(workerHashrate / 1000.0)
                             .average(0.0)
                             .confirmed(balanceJson.getDouble("confirmed"))
                             .unconfirmed(balanceJson.getDouble("unconfirmed"));
    }

    private FluentJson getJson(String jsonStr, String key) {
        FluentJson fj;
        try {
            fj = new FluentJson(new JSONParser().parse(jsonStr));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON parse exception: " + jsonStr); }

        String[] keys = key.split("\\.");
        FluentJson result = FluentJson.copy(fj);
        for(String k : keys)
            result = result.get(k);

        return result;
    }

    private String getServiceData(String url, Crypto crypto) {
        String urlStr  = url.replace("%KEY%", apiKey).replace("%CRYPTO%", crypto.toString());
        String jsonStr = restTemplate.build().getForObject(urlStr, String.class);
        return jsonStr;
    }

}
