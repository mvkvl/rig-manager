package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.notifier.TelegramNotifierBean;

public class WorkerInfoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WorkerInfoRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public void run(String... arg0) throws InterruptedException {

//        System.out.println(String.format(" >>> ICON: 0x%08X", icon));
        telegramNotifier.sendMessage(" <b>wrk-info</b>: service started");

        // in testing environment run application for
        // predefined period
        // in production set duration to 0 or totally remove from config
        if (duration > 0) {
            Thread.sleep(duration);
            ctx.close();
        }
    }

}
