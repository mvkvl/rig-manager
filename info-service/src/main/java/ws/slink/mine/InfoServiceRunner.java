package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import ws.slink.notifier.TelegramNotifierBean;

public class InfoServiceRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InfoServiceRunner.class);

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Override
    public void run(String... arg0) {
        telegramNotifier.sendMessage(" <b>info-service</b>: service started");

//        if (duration > 0) {
//            Thread.sleep(duration);
//            ctx.close();
//        }
    }

}
