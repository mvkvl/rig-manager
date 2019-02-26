package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;


@SpringBootApplication(scanBasePackages={"ws.slink"})
public class PriceInfoApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PriceInfoApplication.class);

    @Bean
    public CommandLineRunner applicationRunner() {
        return new PriceInfoRunner();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("shutting down price info service");
    }

    public static void main(String[] args) {
        logger.info("starting up price info service");
        SpringApplication.run(PriceInfoApplication.class, args);
    }

}
