package ws.slink.mine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ws.slink.mine.info.api.miner.TrexMinerAPI;
import ws.slink.mine.info.api.pool.SuprnovaPoolAPI;

@Configuration
public class InformationSourcesConfig {

    @Bean
    @Scope("prototype")
    public TrexMinerAPI createTrexMinerAPI() {
        return new TrexMinerAPI();
    }

    @Bean
    @Scope("prototype")
    public SuprnovaPoolAPI createSuprnovaPoolAPI() {
        return new SuprnovaPoolAPI();
    }

}
