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
public class IpMonitorApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(IpMonitorApplication.class);

    @Bean
    public CommandLineRunner applicationRunner() {
        return new IpMonitorRunner();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("shutting down ip monitoring service");
    }

    public static void main(String[] args) {
        logger.info("starting ip monitoring service");
        SpringApplication.run(IpMonitorApplication.class, args);
    }

}
