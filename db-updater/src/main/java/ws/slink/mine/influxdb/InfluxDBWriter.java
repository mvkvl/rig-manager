package ws.slink.mine.influxdb;

import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class InfluxDBWriter {

    private static final Logger logger = LoggerFactory.getLogger(InfluxDBWriter.class);

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Value("${influxdb.metric.balance.pool:balance.pool.default}")
    private String balancePoolMetric;

    @Value("${influxdb.metric.balance.wallet:balance.wallet.default}")
    private String balanceWalletMetric;

    @Value("${influxdb.metric.blockchain:blockchain.network.default}")
    private String blockchainMetric;

    @Value("${influxdb.metric.rig:rig.default}")
    private String rigWorkerMetric;

    @Value("${influxdb.metric.pool:pool.worker.default}")
    private String poolWorkerMetric;

    @Value("${influxdb.metric.price:price.default}")
    private String priceMetric;

    @PostConstruct
    public void init() {
        influxDBTemplate.createDatabase();
    }

    public void writeWalletInfo(List<WalletInfo> values) {
        influxDBTemplate.write(
            values.stream().map(w -> Point.measurement(balanceWalletMetric)
                                          .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                          .tag("crypto", w.crypto.toString())
                                          .addField(w.wallet, w.amount)
                                          .build()
            ).collect(Collectors.toList()));
    }

    public void writeNetworkInfo(List<NetworkInfo> values) {
        influxDBTemplate.write(
            values.stream().map(n -> Point.measurement(blockchainMetric)
                                          .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                          .tag("crypto", n.crypto.toString())
                                          .addField("diff", n.diff)
                                          .addField("nethash", n.hashrate)
                                          .build()
            ).collect(Collectors.toList()));
    }

    public void writeRigInfo(List<RigInfo> values) {
        influxDBTemplate.write(getRigWorkerPoints(values));
        influxDBTemplate.write(getRigGPUPoints(values));
    }
    private List<Point> getRigWorkerPoints(List<RigInfo> values) {
        return
                values.stream().map(n -> Point.measurement(rigWorkerMetric + ".worker")
                               .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                               .tag("crypto", n.crypto.toString().toLowerCase())
                               .tag("host", n.rig)
                               .tag("worker", n.worker)
                               .addField("hashrate", n.totalHash)
                               .addField("power", n.totalPower)
                               .addField("efficiency", n.totalEff)
                               .build()
                ).collect(Collectors.toList());
    }
    private List<Point> getRigGPUPoints(List<RigInfo> values) {
        return
        values.stream().map(
                rigInfo -> rigInfo.gpus.stream().map(
                    gpu -> Point.measurement(rigWorkerMetric + ".gpu")
                                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                .tag("crypto", rigInfo.crypto.toString().toLowerCase())
                                .tag("host",   rigInfo.rig)
                                .tag("worker", rigInfo.worker)
                                .tag("gpu", gpu.id+"")
                                .addField("hashrate", gpu.hashrate)
                                .addField("power", gpu.power)
                                .addField("efficiency", gpu.efficiency)
                                .addField("temperature", gpu.temperature)
                                .build()
                ).collect(Collectors.toList())
        ).flatMap(List::stream)
         .collect(Collectors.toList());
    }

    public void writePoolInfo(List<PoolInfo> values) {
        influxDBTemplate.write(
                values.stream().map(n -> Point.measurement(poolWorkerMetric)
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .tag("crypto", n.crypto.toString())
                        .tag("pool", n.pool)
                        .tag("worker", n.worker)
                        .addField("hashrate", n.hashrate)
                        .addField("average", n.average)
                        .build()
                ).collect(Collectors.toList()));
        influxDBTemplate.write(
                values.stream().map(n -> Point.measurement(balancePoolMetric)
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .tag("crypto", n.crypto.toString())
                        .tag("pool", n.pool)
                        .addField("confirmed", n.confirmed)
                        .addField("unconfirmed", n.unconfirmed)
                        .addField("total", n.confirmed + n.unconfirmed)
                        .build()
                ).collect(Collectors.toList()));
    }

    public void writePriceInfo(List<PriceInfo> values) {
        List<Point> points = new ArrayList<>();
        values.stream()
              .forEach( v -> v.getPrices()
                              .entrySet()
                              .stream()
                              .forEach( p ->
                                 points.add(
                                   Point.measurement(priceMetric)
                                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                        .tag("crypto", v.getCrypto().toString().toLowerCase())
                                        .addField(p.getKey().toLowerCase(), p.getValue())
                                        .build())));
        logger.trace("Points: {}", points);
        influxDBTemplate.write(points);
    }

}
