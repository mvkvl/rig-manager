package ws.slink.mine.info.api.miner;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ws.slink.mine.info.model.MinerInfo;
import ws.slink.mine.info.model.GPUInfo;
import ws.slink.mine.info.model.RigInfo;
import ws.slink.mine.tools.FluentJson;

import java.util.stream.Collectors;

public class TrexMinerAPI implements MinerAPI {

    private static final Logger logger = LoggerFactory.getLogger(TrexMinerAPI.class);

    @Autowired
    private RestTemplateBuilder restTemplate;

    @Override
    public RigInfo get(MinerInfo minerInfo) {
        logger.trace("TrexMinerAPI.get(" + minerInfo + ")");
        String urlStr  = "http://" + minerInfo.host + ":" + minerInfo.port + "/summary";

        String jsonStr = restTemplate.build().getForObject(urlStr, String.class);
        FluentJson fj;
        try {
            fj = new FluentJson(new JSONParser().parse(jsonStr));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON parse exception: " + jsonStr); }

        return new RigInfo()
                .crypto(minerInfo.crypto)
                .pool(minerInfo.pool)
                .worker(minerInfo.worker)
                .rig(minerInfo.rig)
                .name(minerInfo.miner)
                .accepted(fj.getInt("accepted_count"))
                .rejected(fj.getInt("rejected_count"))
                .gpus(
                    fj.get("gpus").stream().map(j ->
                            new GPUInfo()
                                    .id(j.getInt("gpu_id"))
                                    .hashrate(j.getDouble("hashrate") / 1000000)
                                    .temperature(j.getDouble("temperature"))
                                    .power(j.getDouble("power"))
                                    .calculate()
                    ).collect(Collectors.toList())
                )
                .calculate();
    }
}
