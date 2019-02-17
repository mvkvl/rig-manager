package ws.slink.mine.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.blockchain.task.NetworkUpdateTask;
import ws.slink.mine.blockchain.task.WalletUpdateTask;
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
public class Updater {

    private static final Logger logger = LoggerFactory.getLogger(Updater.class);

    // walletTask bean provider
    private ObjectProvider<WalletUpdateTask>  walletUpdateTaskProvider;

    // networkTask bean provider
    private ObjectProvider<NetworkUpdateTask> networkUpdateTaskProvider;

    private ScheduledExecutorService executor;

    @Autowired
    private ConfiguredWallets configuredWallets;

    @Autowired
    private ConfiguredBlockchains configuredBlockchains;

    @Autowired
    public Updater(ObjectProvider<WalletUpdateTask> walletUpdateTaskProvider,
                   ObjectProvider<NetworkUpdateTask> networkUpdateTaskProvider) {
        this.walletUpdateTaskProvider  = walletUpdateTaskProvider;
        this.networkUpdateTaskProvider = networkUpdateTaskProvider;
    }

    @Value("${schedule.timeout.network:600}") // by default once in 10 minutes
    private int networkScheduledTimeout;

    @Value("${schedule.timeout.wallet:1800}") // by default once in 30 minutes
    private int walletScheduledTimeout;

    // create tasks for all configured blockchains / wallets
    private List<Runnable> getWalletTasks() {
        List<Runnable> tasks  = new ArrayList<>();
        configuredWallets.currencies().stream().forEach(
            cur -> configuredWallets.wallets(cur).stream().forEach(
                    wlt -> tasks.add(walletUpdateTaskProvider.getObject(cur, wlt))));
        return tasks;
    }

    private List<Runnable> getNetworkTasks() {
        List<Runnable> tasks = new ArrayList<>();
        configuredBlockchains.currencies().stream().forEach(
                cur -> {
                    tasks.add(networkUpdateTaskProvider.getObject(cur));
                }
        );
        return tasks;
    }

    public void run() {
        executor = Executors.newScheduledThreadPool(4);

        // run all tasks
        getWalletTasks().stream().forEach(
                t -> executor.scheduleAtFixedRate(t, 0, walletScheduledTimeout, TimeUnit.SECONDS));

        getNetworkTasks().stream().forEach(
                t -> executor.scheduleAtFixedRate(t, 0, networkScheduledTimeout, TimeUnit.SECONDS));
    }

    @PreDestroy
    private void stopSheduledTasks() {
        if (null != executor) {
            logger.debug("stopping scheduled tasks");
            executor.shutdown();
        }
    }

}
