package ws.slink.mine.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ws.slink.mine.blockchain.Crypto;
import ws.slink.mine.blockchain.task.*;

import java.util.List;


/**
 *
 * Task factories
 *
 */
@Configuration
public class TasksConfig {

    @Bean
    @Scope("prototype")
    public WalletUpdateTask createWalletUpdateTask(Crypto crypto, String walletName) {
        return new WalletUpdateTask(crypto, walletName);
    }

    @Bean
    @Scope("prototype")
    public NetworkUpdateTask createNetworkUpdateTask(Crypto crypto) {
        return new NetworkUpdateTask(crypto);
    }

    @Bean
    @Scope("prototype")
    public FutureWalletUpdateTask createFutureWalletUpdateTask(Crypto crypto, String walletName) {
        return new FutureWalletUpdateTask(crypto, walletName);
    }

    @Bean
    @Scope("prototype")
    public FutureNetworkUpdateTask createFutureNetworkUpdateTask(Crypto crypto) {
        return new FutureNetworkUpdateTask(crypto);
    }

    @Bean
    @Scope("prototype")
    public FutureWalletUpdater createFutureWalletUpdater(List<FutureWalletUpdateTask> tasks) {
        return new FutureWalletUpdater(tasks);
    }

    @Bean
    @Scope("prototype")
    public FutureNetworkUpdater createFutureNetworkUpdater(List<FutureNetworkUpdateTask> tasks) {
        return new FutureNetworkUpdater(tasks);
    }

}
