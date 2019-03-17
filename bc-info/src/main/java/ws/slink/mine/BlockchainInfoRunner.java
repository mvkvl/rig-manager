package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.mine.blockchain.FuturedUpdater;
import ws.slink.mine.blockchain.Updater;
import ws.slink.notifier.TelegramNotifierBean;

public class BlockchainInfoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainInfoRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Autowired
    private Updater updater;

    @Autowired
    private FuturedUpdater futuredUpdater;

    @Autowired
    private TelegramNotifierBean telegramNotifier;

//    @Autowired
//    private ConfiguredPrices configuredPrices;

    @Override
    public void run(String... arg0) {
        telegramNotifier.sendMessage("<b>bc-info</b>: service started");
        futuredUpdater.run();

//        System.out.println(configuredPrices.cryptos());
//        System.out.println(configuredPrices.currencies(Crypto.valueOf("RVN")));

// run application for "duration" ms
//        logger.trace("running BCI app for " + duration + "ms");
//        Thread.sleep(duration);
//        ctx.close();
    }

}
