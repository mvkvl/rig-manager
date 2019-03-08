package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.notifier.TelegramNotifierBean;

public class WorkerManagerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WorkerManagerRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public void run(String... arg0) throws InterruptedException {
        telegramNotifier.sendMessage(" <b>wrk-mgr</b>: service started");

        if (duration > 0) {
            Thread.sleep(duration);
            ctx.close();
        }
    }

}
