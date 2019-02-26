package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.mine.service.NotificationService;

public class IpMonitorRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(IpMonitorRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired private ConfigurableApplicationContext ctx;
    @Autowired private NotificationService notificationService;

    @Override
    public void run(String... arg0) {
        notificationService.notify(" <b>ip-mon</b>: service started");

        // in testing environment run application for
        // predefined period
        // in production set duration to 0 or totally remove from config
//        if (duration > 0) {
//            Thread.sleep(duration);
//            ctx.close();
//        }
    }

}
