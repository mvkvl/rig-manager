package ws.slink.mine.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.info.api.miner.MinerAPI;
import ws.slink.mine.info.api.miner.MinerAPIFactory;
import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.RigInfo;
import ws.slink.mine.model.Miner;
import ws.slink.mine.mq.Sender;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class RigUpdater {

    private static final Logger logger = LoggerFactory.getLogger(RigUpdater.class);

    @Autowired
    private MinerAPIFactory minerAPIFactory;

    @Autowired
    private Sender sender;

    @Value("${amqp.key.rig}")
    private String amqpRigInfoKey;

    @Value("${schedule.maxdelay:1000}")
    private int maxDelay;

    public void update(List<MinerInfo> minerInfoList) {
        logger.trace("RigUpdater.update(" + minerInfoList + ")");
        List<RigInfo> rigData =
            minerInfoList.parallelStream()
                         .map(this::getRigData)
                         .collect(Collectors.toList());
        sender.send(amqpRigInfoKey, rigData);
    }

    private RigInfo getRigData(MinerInfo minerInfo) {
        logger.trace("RigUpdater.getRigData(" + minerInfo + ")");

        // random delay
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MinerAPI minerAPI = minerAPIFactory.get(Miner.valueOf(minerInfo.miner.toUpperCase()));
        return minerAPI.get(minerInfo);
    }

}
