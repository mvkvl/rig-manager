package ws.slink.mine;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import ws.slink.mine.info.Updater;

public class WorkerInfoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WorkerInfoRunner.class);

    @Value("${test.duration:0}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

//    @Autowired
//    private Updater updater;
//    @Autowired
//    private FuturedUpdater futuredUpdater;

    @Override
    public void run(String... arg0) throws InterruptedException {
        // in testing environment run application for
        // predefined period
        // in production set duration to 0 or totally remove from config
        if (duration > 0) {
            Thread.sleep(duration);
            ctx.close();
        }
    }

}
