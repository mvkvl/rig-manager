package ws.slink.mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;


@SpringBootApplication
//@EnableScheduling
public class InfoReceiverApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InfoReceiverApplication.class);

//    @Profile("!usage_message")
    @Bean
    public CommandLineRunner blockChainInfo() {
        return new InfoReceiverRunner();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("shutting down block chain info service");
    }

    public static void main(String[] args) {
        logger.info("starting block chain info service");
        SpringApplication.run(InfoReceiverApplication.class, args);
    }

}
