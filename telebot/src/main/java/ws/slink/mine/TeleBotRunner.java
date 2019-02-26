package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import ws.slink.notifier.TelegramNotifierBean;

public class TeleBotRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TeleBotRunner.class);

//    @Value("${test.duration:0}")
//    private int duration;

//    @Autowired
//    private ConfigurableApplicationContext ctx;

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Override
    public void run(String... arg0) {

        telegramNotifier.sendMessage(" <b>telebot</b>: service started");

        // in testing environment run application for
        // predefined period
        // in production set duration to 0 or totally remove from config
//        if (duration > 0) {
//            Thread.sleep(duration);
//            ctx.close();
//        }
    }

}
