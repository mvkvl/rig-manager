package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.notifier.TelegramNotifierBean;

public class ManagementServiceRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ManagementServiceRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Autowired
    private TelegramNotifierBean telegramNotifier;

//    @Autowired
//    private Updater updater;

//    @Autowired
//    private FuturedUpdater futuredUpdater;

//    @Autowired
//    private ConfiguredPrices configuredPrices;

    @Override
    public void run(String... arg0) throws InterruptedException {
        telegramNotifier.sendMessage("<b>mgmt-service</b>: service started");
        // run application for "duration" ms
        if (duration > 0) {
            logger.trace("running BCI app for " + duration + "ms");
            Thread.sleep(duration);
            ctx.close();
        }
    }
}
