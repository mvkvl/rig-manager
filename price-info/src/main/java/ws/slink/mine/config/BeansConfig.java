package ws.slink.mine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ws.slink.mine.model.Crypto;
import ws.slink.mine.price.PriceUpdateTask;

@Configuration
public class BeansConfig {

    @Bean
    @Scope("prototype")
    public PriceUpdateTask createPriceUpdateTask(Crypto crypto,
                                                 String currencies) {
        return new PriceUpdateTask(crypto, currencies);
    }

}
