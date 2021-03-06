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
public class InfoServiceApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InfoServiceApplication.class);

    @Bean
    public CommandLineRunner applicationRunner() {
        return new InfoServiceRunner();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("shutting down info service");
    }

    public static void main(String[] args) {
        logger.info("starting info service");
        SpringApplication.run(InfoServiceApplication.class, args);
    }

}
