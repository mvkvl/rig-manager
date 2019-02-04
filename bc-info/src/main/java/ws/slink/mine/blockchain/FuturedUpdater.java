package ws.slink.mine.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.blockchain.task.FutureNetworkUpdateTask;
import ws.slink.mine.blockchain.task.FutureNetworkUpdater;
import ws.slink.mine.blockchain.task.FutureWalletUpdateTask;
import ws.slink.mine.blockchain.task.FutureWalletUpdater;
import ws.slink.mine.conf.ConfiguredBlockchains;
import ws.slink.mine.conf.ConfiguredWallets;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 *  Information update manager/executor
 *
 */
@Component
public class FuturedUpdater {

    private static final Logger logger = LoggerFactory.getLogger(FuturedUpdater.class);

    // bean providers
    private ObjectProvider<FutureWalletUpdateTask>  futureWalletUpdateTaskProvider;
    private ObjectProvider<FutureWalletUpdater>     futureWalletUpdaterProvider;
    private ObjectProvider<FutureNetworkUpdateTask> futureNetworkUpdateTaskProvider;
    private ObjectProvider<FutureNetworkUpdater>    futureNetworkUpdaterProvider;

    @Autowired
    private ConfiguredWallets configuredWallets;

    @Autowired
    private ConfiguredBlockchains configuredBlockchains;

    @Autowired
    public FuturedUpdater(ObjectProvider<FutureWalletUpdateTask>  futureWalletUpdateTaskProvider,
                          ObjectProvider<FutureNetworkUpdateTask> futureNetworkUpdateTaskProvider,
                          ObjectProvider<FutureWalletUpdater>     futureWalletUpdaterProvider,
                          ObjectProvider<FutureNetworkUpdater>    futureNetworkUpdaterProvider) {
        this.futureWalletUpdateTaskProvider  = futureWalletUpdateTaskProvider;
        this.futureWalletUpdaterProvider     = futureWalletUpdaterProvider;
        this.futureNetworkUpdateTaskProvider = futureNetworkUpdateTaskProvider;
        this.futureNetworkUpdaterProvider    = futureNetworkUpdaterProvider;
    }

    @Value("${schedule.timeout.network:600}") // by default once in 10 minutes
    private int networkScheduledTimeout;

    @Value("${schedule.timeout.wallet:1800}") // by default once in 30 minutes
    private int walletScheduledTimeout;

    private ScheduledExecutorService   executor;

    private List<FutureWalletUpdateTask> getFutureWalletTasks() {
        List<FutureWalletUpdateTask> tasks = new ArrayList<>();
        configuredWallets.currencies().stream().forEach(
                cur -> configuredWallets.wallets(cur).stream().forEach(
                        wlt -> tasks.add(futureWalletUpdateTaskProvider.getObject(cur, wlt))));
        return tasks;
    }
    private List<FutureNetworkUpdateTask> getFutureNetworkTasks() {
        List<FutureNetworkUpdateTask> tasks = new ArrayList<>();
        configuredBlockchains.currencies().stream().forEach(
                cur -> tasks.add(futureNetworkUpdateTaskProvider.getObject(cur)));
        return tasks;
    }

    public void run() {
        executor = Executors.newScheduledThreadPool(4);
        executor.scheduleAtFixedRate(futureWalletUpdaterProvider.getObject(getFutureWalletTasks()),
                0, walletScheduledTimeout, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(futureNetworkUpdaterProvider.getObject(getFutureNetworkTasks()),
                0, networkScheduledTimeout, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void stopSheduledTasks() {
        if (null != executor) {
            logger.debug("stopping scheduled tasks");
            executor.shutdown();
        }
    }

}
