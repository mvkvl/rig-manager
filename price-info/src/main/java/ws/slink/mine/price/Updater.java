package ws.slink.mine.price;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.slink.mine.config.ConfiguredPrices;
import ws.slink.mine.model.PriceInfo;
import ws.slink.mine.mq.Sender;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class Updater {

    private List<PriceUpdateTask> tasks;
    private List<CompletableFuture<Optional<PriceInfo>>> futures;

    @Value("${amqp.key.price}")
    private String amqpKey;

    @Autowired
    private ConfiguredPrices configuredPrices;

    @Autowired
    private Sender sender;

    private ObjectProvider<PriceUpdateTask> priceUpdateTaskProvider;;

    public Updater(ObjectProvider<PriceUpdateTask> priceUpdateTaskProvider) {
        this.priceUpdateTaskProvider = priceUpdateTaskProvider;
    }

    @PostConstruct
    private void prepare() {
        tasks = new ArrayList<>();
        configuredPrices.cryptos().stream().forEach(
                c -> tasks.add(priceUpdateTaskProvider
                                .getObject(c,
                                           String.join(",", configuredPrices.currencies(c))))
        );
    }

    @Scheduled(initialDelay = 1000, fixedRateString = "${schedule.timeout.price}")
    private void update() {
        try {
            futures = tasks.stream()
                           .map(CompletableFuture::supplyAsync)
                           .collect(Collectors.toList());
            List<PriceInfo> info = FutureTools.all(futures);
            sender.send(amqpKey, info);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
