package ws.slink.mine.blockchain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ws.slink.mine.blockchain.info.NetworkInfoRequest;
import ws.slink.mine.model.NetworkInfo;
import ws.slink.mine.mq.Sender;
import ws.slink.mine.type.Crypto;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 *  Blockchain network information update task
 *
 */
public class NetworkUpdateTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUpdateTask.class);

    @Value("${schedule.maxdelay:3}")
    private int maxDelay;

    @Value("${amqp.key.network}")
    private String amqpKey;

    @Autowired
    private NetworkInfoRequest networkInfo ;

    @Autowired
    private Sender sender;

    private Crypto crypto;

    public NetworkUpdateTask(Crypto crypto) {
        logger.trace("created task: [" + crypto + "]");
        this.crypto = crypto;
    }

    @Override
    public void run() {
        // random delay before start
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);
        } catch(InterruptedException e) {
            logger.warn("sleep interrupted");
        }

        Optional<NetworkInfo> info = Optional.empty();
        // get balance for given crypto & wallet
        try {
            info = networkInfo.get(crypto);
        } catch (Exception ex) {
            logger.error("update error for [" + crypto + "]: " + ex.getMessage());
        }

        info.ifPresent(v -> {
            logger.trace("[" + v + "]");
            sender.send(amqpKey, v);
        });
    }
}
