package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class InfoServiceRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InfoServiceRunner.class);

//    @Value("${test.duration:0}")
//    private int duration;

//    @Autowired
//    private ConfigurableApplicationContext ctx;

//    @Autowired
//    private InfluxDBReader influxDBReader;


//    @Autowired
//    private Updater updater;
//    @Autowired
//    private FuturedUpdater futuredUpdater;

    @Override
    public void run(String... arg0) {
        // in testing environment run application for
        // predefined period
        // in production set duration to 0 or totally remove from config

//        influxDBReader.read();

//        if (duration > 0) {
//            Thread.sleep(duration);
//            ctx.close();
//        }
    }

}
