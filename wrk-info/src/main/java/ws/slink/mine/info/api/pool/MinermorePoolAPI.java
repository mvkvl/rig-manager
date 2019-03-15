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
import ws.slink.mine.tools.FluentJson;

public class MinermorePoolAPI implements PoolAPI {


    private final String BALANCE_URL = "https://minermore.com/api/wallet?address=%WALLET%";
    private final String WORKER_URL  = "https://minermore.com/api/workers?wallet=%WALLET%";

    private static final Logger logger = LoggerFactory.getLogger(MinermorePoolAPI.class);

    @Value("${api.pool.key.minermore.balance}")
    private String apiBalanceKey;

    @Value("${api.pool.key.minermore.worker}")
    private String apiWorkerKey;

    @Autowired
    private RestTemplateBuilder restTemplate;

    @Override
    public PoolInfo get(MinerInfo minerInfo) {
        logger.trace("MinermorePoolAPI.get(" + minerInfo + ")");

        logger.trace(minerInfo.crypto.toString().toUpperCase());

        FluentJson balanceJson = getJson(getServiceData(BALANCE_URL, apiBalanceKey), minerInfo.crypto.toString().toUpperCase());
        FluentJson workersJson = getJson(getServiceData(WORKER_URL , apiWorkerKey), "workers");

        logger.trace(balanceJson.toString());
        logger.trace(workersJson.toString());

        double workerHashrate =
                workersJson.stream()
                        .filter(item -> item.getString("name").toLowerCase().contains(minerInfo.worker.toLowerCase()))
                        .map(item -> item.getDouble("hashrate"))
                        .findFirst().orElseGet(() -> Double.valueOf(0));

        double averageWorkerHashrate =
                workersJson.stream()
                        .filter(item -> item.getString("name").toLowerCase().contains(minerInfo.worker.toLowerCase()))
                        .map(item -> item.getDouble("hashrate_avg"))
                        .findFirst().orElseGet(() -> Double.valueOf(0));

        logger.trace("HR : " + workerHashrate);
        logger.trace("AHR: " + averageWorkerHashrate);

        return new PoolInfo()
                .crypto(minerInfo.crypto)
                .pool(minerInfo.pool)
                .worker(minerInfo.worker)
                .hashrate(workerHashrate / 1000000.0)
                .average(averageWorkerHashrate / 1000000.0)
                .confirmed(Double.parseDouble(balanceJson.getString("mature")) + Double.parseDouble(balanceJson.getString("balance")))
                .unconfirmed(Double.parseDouble(balanceJson.getString("immature")));
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

    private String getServiceData(String url, String key) {
        String urlStr  = url.replace("%WALLET%", key);
        String jsonStr = restTemplate.build().getForObject(urlStr, String.class);
        return jsonStr;
    }



}
