package ws.slink.mine.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.slink.mine.config.ConfiguredPortfolio;
import ws.slink.mine.influxdb.InfluxDBReader;
import ws.slink.mine.influxdb.InfluxDBWriter;
import ws.slink.mine.type.Crypto;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class PortfolioUpdater {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioUpdater.class);

    @Autowired
    private ConfiguredPortfolio configuredPortfolio;

    @Autowired
    private InfluxDBReader reader;

    @Autowired
    private InfluxDBWriter writer;

    @Scheduled(initialDelay = 15000, fixedRateString = "1800000") // once in 30 minutes
    private void updatePortfolio() {
        Map<Crypto, Map<String, Double>> data = new HashMap<>();
        configuredPortfolio
            .cryptos()
            .stream()
            .forEach( crypto -> configuredPortfolio
                .currencies(crypto)
                .stream()
                .forEach( currency -> {
                    Double balance = (reader.cryptoBalance(crypto, "holding")
                                   +  reader.cryptoBalance(crypto, "mining"))
                                   *  reader.cryptoPrice(crypto, currency);
                    if (data.containsKey(crypto)) {
                        data.get(crypto).put(currency, balance);
                    }
                    else {
                        Map<String, Double> map = new HashMap<>();
                        map.put(currency, balance);
                        data.put(crypto, map);
                    }
                })
            );
        writer.writePortfolioInfo(data);
    }



}
