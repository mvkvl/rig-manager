package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import ws.slink.mine.influxdb.InfluxDBReader;


@SpringBootApplication
public class SnapshotInfoServiceApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotInfoServiceApplication.class);

//    @Profile("!usage_message")
    @Bean
    public CommandLineRunner runner() {
        return new SnapshotInfoServiceRunner();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("shutting down snapshot info service");
    }

    public static void main(String[] args) {
        logger.info("starting snapshot info service");
        SpringApplication.run(SnapshotInfoServiceApplication.class, args);
    }

}
