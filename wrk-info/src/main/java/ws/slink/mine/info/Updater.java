package ws.slink.mine.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.slink.mine.info.model.MinerInfo;
import ws.slink.mine.info.conf.MinerInfoLoader;

import java.util.List;

@Component
@EnableScheduling
public class Updater {

    private static final Logger logger = LoggerFactory.getLogger(Updater.class);

    @Autowired
    private RigUpdater rigUpdater;

    @Autowired
    private PoolUpdater poolUpdater;

    @Autowired
    private MinerInfoLoader minerInfoLoader;

    private List<MinerInfo> minerInfoList;

    @Scheduled(initialDelay = 1000, fixedRate = 60000) // reload config once in a minute
    private void readConfig() {
        synchronized (this) {
            logger.trace("running miners config update task");
            minerInfoList = minerInfoLoader.get();
        }
    }

    @Scheduled(initialDelay = 5000, fixedRateString = "${schedule.timeout.rig}")
    public void updateRigInfo() {
        try {
            logger.trace("rig update attempt");
            if (null != minerInfoList && !minerInfoList.isEmpty())
                rigUpdater.update(minerInfoList);
            else
                logger.trace("empty input commands");
        } catch (Exception ex) {
            logger.error("Exception processing rig information: " + ex.getMessage());
//            ex.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 5000, fixedRateString = "${schedule.timeout.pool}")
    public void updatePoolInfo() {
        try {
            logger.trace("pool update attempt");
            if (null != minerInfoList && !minerInfoList.isEmpty())
                poolUpdater.update(minerInfoList);
            else
                logger.trace("empty input commands");
        } catch (Exception ex) {
            logger.error("Exception processing pool information: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}