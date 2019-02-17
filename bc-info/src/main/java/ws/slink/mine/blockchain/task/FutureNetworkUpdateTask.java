package ws.slink.mine.blockchain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.blockchain.info.NetworkInfoRequest;
import ws.slink.mine.model.NetworkInfo;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 *
 *  Blockchain network information update task
 *
 */
public class FutureNetworkUpdateTask implements Supplier<Optional<NetworkInfo>> {

    private static final Logger logger = LoggerFactory.getLogger(FutureNetworkUpdateTask.class);

    @Value("${schedule.maxdelay:3}")
    private int maxDelay;

    @Autowired
    private NetworkInfoRequest networkInfo ;

    private Crypto crypto;

    public FutureNetworkUpdateTask(Crypto crypto) {
        logger.trace("created task: [" + crypto + "]");
        this.crypto = crypto;
    }

    @Override
    public Optional<NetworkInfo> get() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);
        } catch(InterruptedException e) {
            logger.warn("sleep interrupted");
        }

        Optional<NetworkInfo> info = Optional.empty();

        // get balance for given crypto & wallet
        try {
            info = networkInfo.get(crypto);
            info.ifPresent(v -> logger.trace("[" + v + "]"));
        } catch (Exception ex) {
            logger.error("update error for [" + crypto + "]: " + ex.getMessage());
        }

        return info;
    }
}
