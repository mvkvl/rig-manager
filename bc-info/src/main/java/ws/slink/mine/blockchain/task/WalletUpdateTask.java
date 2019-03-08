package ws.slink.mine.blockchain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.blockchain.info.WalletInfoRequest;
import ws.slink.mine.model.WalletInfo;
import ws.slink.mine.mq.Sender;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 *  Wallet information update task
 *
 */
public class WalletUpdateTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WalletUpdateTask.class);

    @Autowired
    private WalletInfoRequest walletInfo;

    @Value("${schedule.maxdelay}")
    private int maxDelay;

    @Value("${amqp.key.wallet}")
    private String amqpKey;

    @Autowired
    private Sender sender;

    private String wallet;
    private Crypto crypto;

    public WalletUpdateTask(Crypto crypto, String walletName) {
        logger.trace("created task: [" + crypto + " : " + walletName + "]");
        this.crypto = crypto;
        this.wallet = walletName;
    }

    @Override
    public void run() {
        // random delay before start
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);
        } catch(InterruptedException e) {
            logger.warn("sleep interrupted");
        }

        Optional<WalletInfo> balance = Optional.empty();
        // get balance for given crypto & wallet
        try {
            balance = walletInfo.balance(crypto, wallet);
        } catch (Exception ex) {
            logger.error("update error for [" + crypto + ", " + wallet + "]: " + ex.getMessage());
        }

        balance.ifPresent(v -> {
            logger.trace("[" + v + "]");
            sender.send(amqpKey, v);
        });

    }
}
