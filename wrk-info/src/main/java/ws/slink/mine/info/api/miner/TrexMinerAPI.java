package ws.slink.mine.info.api.miner;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ws.slink.mine.info.conf.MinerInfo;
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

/*
{
        "accepted_count": 51522,
        "active_pool": {
          "difficulty": 56.92665116,
          "ping": 261,
          "retries": 7,
          "url": "stratum+tcp://rvn.suprnova.cc:6667",
          "user": "minervl.sn_rvn_zenemy"
        },
        "algorithm": "x16r",
        "api": "3.0",
        "cuda": "9.10",
        "description": "T-Rex NVIDIA GPU miner",
        "difficulty": 53724.55074240396,
        "gpu_total": 10,
        "gpus": [
        {
          "device_id": 0,
          "efficiency": "163kH/W",
          "fan_speed": 18,
          "gpu_id": 0,
          "hashrate": 26760056,
          "hashrate_day": 30264431,
          "hashrate_hour": 30069206,
          "hashrate_instant": 26674842,
          "hashrate_minute": 26083096,
          "intensity": 21,
          "name": "GeForce GTX 1080 Ti",
          "power": 176,
          "temperature": 36,
          "vendor": "EVGA"
        },
        {
          "device_id": 1,
          "efficiency": "153kH/W",
          "fan_speed": 18,
          "gpu_id": 1,
          "hashrate": 27169257,
          "hashrate_day": 30788871,
          "hashrate_hour": 30577879,
          "hashrate_instant": 27368159,
          "hashrate_minute": 26503908,
          "intensity": 21,
          "name": "GeForce GTX 1080 Ti",
          "power": 184,
          "temperature": 38,
          "vendor": "EVGA"
        },
        {
          "device_id": 2,
          "efficiency": "164kH/W",
          "fan_speed": 18,
          "gpu_id": 2,
          "hashrate": 26729154,
          "hashrate_day": 30140509,
          "hashrate_hour": 30030351,
          "hashrate_instant": 26816005,
          "hashrate_minute": 26071870,
          "intensity": 21,
          "name": "GeForce GTX 1080 Ti",
          "power": 175,
          "temperature": 36,
          "vendor": "EVGA"
        }
        ],
        "hashrate": 271232220,
        "hashrate_day": 306231584,
        "hashrate_hour": 305081880,
        "hashrate_minute": 264506802,
        "name": "t-rex",
        "os": "linux",
        "rejected_count": 293,
        "sharerate": 6.105,
        "sharerate_average": 6.906,
        "solved_count": 0,
        "success": 1,
        "ts": 1548945905,
        "uptime": 447624,
        "version": "0.9.1"
        }
*/