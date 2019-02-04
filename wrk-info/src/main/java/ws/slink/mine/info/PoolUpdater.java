package ws.slink.mine.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.info.api.pool.PoolAPI;
import ws.slink.mine.info.api.pool.PoolAPIFactory;
import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.PoolInfo;
import ws.slink.mine.model.Pool;
import ws.slink.mine.mq.Sender;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class PoolUpdater {

    private static final Logger logger = LoggerFactory.getLogger(PoolUpdater.class);

    @Autowired
    private PoolAPIFactory poolAPIFactory;

    @Autowired
    private Sender sender;

    @Value("${schedule.maxdelay:1000}")
    private int maxDelay;

    @Value("${amqp.key.pool}")
    private String amqpPoolInfoKey;

    public void update(List<MinerInfo> minerInfoList) {
        logger.trace("PoolUpdater.update(" + minerInfoList + ")");
        List<PoolInfo> poolData =
                minerInfoList.parallelStream()
                        .map(this::getPoolData)
                        .collect(Collectors.toList());
        sender.send(amqpPoolInfoKey, poolData);
    }

    private PoolInfo getPoolData(MinerInfo minerInfo) {
        logger.trace("RigUpdater.getRigData(" + minerInfo + ")");
        // random delay
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PoolAPI poolAPI = poolAPIFactory.get(Pool.valueOf(minerInfo.pool.toUpperCase()));
        return poolAPI.get(minerInfo);
    }


}
