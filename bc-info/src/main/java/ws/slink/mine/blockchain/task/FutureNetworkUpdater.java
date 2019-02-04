package ws.slink.mine.blockchain.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ws.slink.mine.model.NetworkInfo;
import ws.slink.mine.mq.Sender;
import ws.slink.mine.tools.FutureTools;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FutureNetworkUpdater implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FutureNetworkUpdater.class);

    @Value("${amqp.key.network}")
    private String amqpNetworkKey;

    @Autowired
    private Sender sender;

    private List<FutureNetworkUpdateTask> tasks;
    private List<CompletableFuture<Optional<NetworkInfo>>> futures;

    public FutureNetworkUpdater(List<FutureNetworkUpdateTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        try {
            futures = tasks.stream()
                    .map(CompletableFuture::supplyAsync)
                    .collect(Collectors.toList());
            List<NetworkInfo> info = FutureTools.all(futures);
//            System.out.println("Network Info:");
//            info.stream().forEach(System.out::println);
            sender.send(amqpNetworkKey, info);
//            System.out.println("----------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PreDestroy
    private void stopActiveFutures() {
        if (null != futures) {
            logger.debug("stopping network updater futures");
            futures.stream().forEach(f -> f.cancel(true));
            futures = null;
        }
    }

}
