package ws.slink.mine.blockchain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ws.slink.mine.model.WalletInfo;
import ws.slink.mine.mq.Sender;
import ws.slink.mine.tools.FutureTools;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FutureWalletUpdater implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FutureWalletUpdater.class);

    @Value("${amqp.key.wallet}")
    private String amqpWalletKey;

    @Autowired
    private Sender sender;

    private List<FutureWalletUpdateTask> tasks;
    private List<CompletableFuture<Optional<WalletInfo>>> futures;

    public FutureWalletUpdater(List<FutureWalletUpdateTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        try {
            futures = tasks.stream()
                    .map(CompletableFuture::supplyAsync)
                    .collect(Collectors.toList());
            List<WalletInfo> info = FutureTools.all(futures);
//            System.out.println("Wallet Info:");
//            info.stream().forEach(System.out::println);
            sender.send(amqpWalletKey, info);
//            System.out.println("----------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void stop() {
        if (null != futures) {
            logger.debug("stopping wallet updater futures");
            futures.stream().forEach(f -> f.cancel(true));
            futures = null;
        }
    }

    @PreDestroy
    private void stopActiveFutures() {
        if (null != futures) {
            logger.debug("stopping wallet updater futures");
            futures.stream().forEach(f -> f.cancel(true));
//            for(CompletableFuture f : futures) {
//                if (!f.isCancelled() && !f.isCompletedExceptionally() && !f.isDone()) {
//                    f.cancel(true);
//                }
//            }
            futures = null;
        }
    }

}
