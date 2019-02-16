package ws.slink.mine.aggregator;

import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.BalanceData;
import ws.slink.mine.model.Crypto;
import ws.slink.mine.model.GPUData;
import ws.slink.mine.model.WorkerData;
import ws.slink.mine.model.response.RigDataResponse;
import ws.slink.mine.model.response.WorkerDataResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class InformationAggregator {

    private static final Logger logger = LoggerFactory.getLogger(InformationAggregator.class);

    private static final String QUERY_PERIOD = "5m";
    List<String> periods = Arrays.asList(QUERY_PERIOD, "1d", "7d", "30d");

    @Value("${schedule.timeout.maxdelay:15}")
    private int maxDelay;

    @Value("${spring.influxdb.database}")
    private String influxDatabaseName;

    @Value("${spring.influxdb.retention-policy}")
    private String influxRetentionPolicy;

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    private Map<String, List<? extends Object>> data = new ConcurrentHashMap<>();

    /* ----------------- GETTERS ----------------------- */
    public List<WorkerDataResponse> getWorkerData() {
        List<WorkerData> workers = (List<WorkerData>) data.get("worker");
        if (null == workers) return Collections.EMPTY_LIST;
        Map<String, WorkerDataResponse> result = new HashMap<>();
        workers.stream().forEach(w -> {
            WorkerDataResponse wdr = result.get(w.worker());
            if (null == wdr)
                result.put(w.worker(), WorkerDataResponse.valueOf(w));
            else
                wdr.add(w);
        });
        return new ArrayList(result.values());
//        return (null == result ) ? Collections.EMPTY_LIST : result ;
    }
    public List<RigDataResponse> getGPUData() {
        List<GPUData> gpus = (List<GPUData>) data.get("gpu");
        if (null == gpus ) return Collections.EMPTY_LIST;
        Map<String, RigDataResponse> rigs = new HashMap<>();
        gpus.stream().forEach(g -> {
            RigDataResponse rdr = rigs.get(g.rig());
            if (null == rdr)
                rigs.put(g.rig(), RigDataResponse.valueOf(g));
            else
                rdr.add(g);
        });
        return new ArrayList(rigs.values());
    }
    public List<BalanceData> getBalanceData() {
        List<BalanceData> result = (List<BalanceData>) data.get("balance");
        return (null == result ) ? Collections.EMPTY_LIST : result ;
    }

    /* ------------- SCHEDULED TASKS ------------------- */
    @Scheduled(fixedDelayString = "${schedule.timeout.agg}")
    private void updateWorkersData() {
        // random delay
        try {Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);}
        catch (InterruptedException e) {}
        // update data
        List<WorkerData> aggregatedWorkerData = getWorkers();
        periods.stream().forEach(p -> {
            String key = translateKey(p);
            aggregatedWorkerData.stream().forEach(wd -> {
                wd.rigRate(key, getAverageWorkerHashrate("rig", "host", wd.rig, wd.worker, p));
                wd.poolRate(key, getAverageWorkerHashrate("pool", "pool", wd.pool, wd.worker, p));
            });
        });
        data.put("worker", aggregatedWorkerData);
        logger.trace("got worker data [{}]", new Object[]{data.get("worker").size()});
    }
    @Scheduled(fixedDelayString = "${schedule.timeout.agg}")
    private void updateGPUData() {
        // random delay
        try {Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);}
        catch (InterruptedException e) {}
        // update data
        List<GPUData> aggregatedGPUData = getGPUs();
        periods.stream().forEach(p -> {
            String key = translateKey(p);
            aggregatedGPUData.stream().forEach(gd -> {
                GPUData ngd = getGPUData(gd.rig, gd.worker, gd.id + "", p);
                gd.hashrate(key, ngd.hashrate(p));
                gd.temperature(key, ngd.temperature(p));
                gd.power(key, ngd.power(p));
            });
        });
        data.put("gpu", aggregatedGPUData);
        logger.trace("got GPU data [{}]", new Object[]{data.get("gpu").size()});
    }
    @Scheduled(fixedDelayString = "${schedule.timeout.agg}")
    private void updateBalanceData() {
        // random delay
        try {Thread.sleep(ThreadLocalRandom.current().nextInt(maxDelay) * 1000);}
        catch (InterruptedException e) {}
        List<WorkerData> wrk = (List<WorkerData>) data.get("worker");
        if (null != wrk) {
            data.put("balance", getBalances(wrk));
            logger.trace("got balance data [{}]", new Object[]{data.get("balance").size()});
        } else {
            logger.trace("no balance data");
        }
    }

    /* ------------------ TOOLS ------------------------ */
    private String translateKey(String input) {
        switch(input) {
            case QUERY_PERIOD: return "cur";
            case "1d" :
            case "1D" :
            case "24h":
            case "24H": return "1d";
            case "7d" :
            case "7D" : return "1w";
            case "30d":
            case "30D": return "1m";
            default   : return input;
        }
    }

    /* --------------- COMMON DATA --------------------- */
    private List<String> tags(String measurement, String key) {
        Query query = new Query("SHOW TAG VALUES FROM \"" + measurement +
                                "\" WITH KEY = \"" + key + "\"", influxDatabaseName);
        QueryResult res = influxDBTemplate.query(query);
        return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
    }

    /* --------------- WORKER DATA --------------------- */
    private List<WorkerData> getWorkers() {
        List<String> rw = getActiveRigWorkers(tags("rig.worker", "host"),
                                     tags("rig.worker", "worker")); //rigs(), workers());
        List<String> pw = getActivePoolWorkers(tags("pool.worker", "pool"),
                                               tags("rig.worker", "worker")); //pools(), workers());
        Map<String, WorkerData> w = new HashMap<>();
        rw.stream().forEach(v -> {
            String [] ss = v.split("\\.", 2);
            w.put(ss[1], new WorkerData().worker(ss[1]).rig(ss[0]));
        });
        pw.stream().forEach(v -> {
            String [] ss = v.split("\\.", 2);
            if (w.containsKey(ss[1])) {
                w.get(ss[1]).pool(ss[0]);
            } else {
                w.put(ss[1], new WorkerData().worker(ss[1]).pool(ss[0]));
            }
        });
        return w.values().stream().filter(v -> StringUtils.isNotBlank(v.pool()) && StringUtils.isNotBlank(v.rig)).collect(Collectors.toList());
    }
    private List<String> getActiveRigWorkers(List<String> rigs, List<String> workers) {
        List<String> result = new ArrayList<>();
        rigs.stream().forEach( r -> {
            workers.stream().forEach( w -> {
                if (getRigWorkerHashrate(r, w) > 0.0)
                    result.add(r + "." + w);
            });
        });
        return result;
    }
    private List<String> getActivePoolWorkers(List<String> pools, List<String> workers) {
        List<String> result = new ArrayList<>();
        pools.stream().forEach( p -> {
            workers.stream().forEach( w -> {
                if (getPoolWorkerHashrate(p, w) > 0.0)
                    result.add(p + "." + w);
            });
        });
        return result;
    }
    private double getRigWorkerHashrate(String rig, String worker) {
        return getWorkerHashrate("rig", "host", rig, worker);
    }
    private double getPoolWorkerHashrate(String pool, String worker) {
        return getWorkerHashrate("pool", "pool", pool, worker);
    }
    private double getWorkerHashrate(String msmt, String src, String srcName, String worker) {
        Query query = BoundParameterQuery
                .QueryBuilder
                .newQuery("SELECT last(\"hashrate\") as \"last_hashrate\" FROM " +
                        "\"" + influxRetentionPolicy + "\".\"" + msmt + ".worker\" " +
                        "WHERE time > now() - " + QUERY_PERIOD +" AND " + src + " = $srcName AND worker = $worker")
                .forDatabase(influxDatabaseName)
                .bind("srcName", srcName)
                .bind("worker", worker)
                .create();
        QueryResult results = influxDBTemplate.query(query);
        List<QueryResult.Series> sl = results.getResults().get(0).getSeries();
        return (null == sl) ? 0.0 : Double.parseDouble(sl.get(0).getValues().get(0).get(1).toString());
    }
    private double getAverageWorkerHashrate(String msmt, String src, String srcName, String worker, String period) {
        Query query = BoundParameterQuery
                .QueryBuilder
                .newQuery("SELECT mean(\"hashrate\") as \"mean_hashrate\" FROM " +
                        "\"" + influxRetentionPolicy + "\".\"" + msmt + ".worker\" " +
                        "WHERE time > now() - " + period + " AND " + src + " = $srcName " +
                        "AND worker = $worker"
                )
                .forDatabase(influxDatabaseName)
                .bind("srcName", srcName)
                .bind("worker", worker)
                .create();
        QueryResult results = influxDBTemplate.query(query);
        List<QueryResult.Series> sl = results.getResults().get(0).getSeries();
        return (null == sl) ? 0.0 : Double.parseDouble(sl.get(0).getValues().get(0).get(1).toString());
    }

    /* ----------------- GPU DATA ---------------------- */
    private List<GPUData> getGPUs() {
        List<GPUData> g = new ArrayList<>();
        getActiveRigWorkerGPU( tags("rig.worker", "host"),   // rigs(),
                               tags("rig.worker", "worker"), // workers(),
                               tags("rig.gpu", "gpu")        // gpus()
                             ).stream().forEach(v -> {
            String [] ss = v.split("\\.", 3);
            g.add(new GPUData().id(Integer.parseInt(ss[2])).worker(ss[1]).rig(ss[0]));
        });
        g.sort((a,b) -> a.id() - b.id());
        return g;
    }
    private List<String> getActiveRigWorkerGPU(List<String> rigs, List<String> workers, List<String> gpus) {
        List<String> result = new ArrayList<>();
        rigs.stream().forEach( r -> {
            workers.stream().forEach( w -> {
                gpus.stream().forEach( g -> {
                    if (getGPUData(r, w, g, QUERY_PERIOD).id >= 0) {
                        result.add(r + "." + w + "." + g);
                    }});
            });
        });
        return result;
    }
    private GPUData getGPUData(String host, String worker, String gpu, String period) {
        Query query = BoundParameterQuery
                .QueryBuilder
                .newQuery("SELECT mean(\"hashrate\") as \"mean_hashrate\", mean(\"temperature\") as \"mean_temp\", mean(\"power\") as \"mean_power\" FROM " +
                        "\"" + influxRetentionPolicy + "\".\"rig.gpu\" " +
                        "WHERE time > now() - " + period + " AND host = $host AND gpu = $gpu AND worker = $worker")
                .forDatabase(influxDatabaseName)
                .bind("host", host)
                .bind("worker", worker)
                .bind("gpu", gpu)
                .create();
        QueryResult results = influxDBTemplate.query(query);
        List<QueryResult.Series> sl = results.getResults().get(0).getSeries();

        GPUData result = new GPUData();
        if (null != sl) {
            result.id     = Integer.parseInt(gpu);
            result.rig    = host;
            result.worker = worker;
            result.hashrate.put(period, Double.parseDouble(sl.get(0).getValues().get(0).get(1).toString()));
            result.temperature.put(period, Double.parseDouble(sl.get(0).getValues().get(0).get(2).toString()));
            result.power.put(period, Double.parseDouble(sl.get(0).getValues().get(0).get(3).toString()));
        }

        return result;
    }

    /* --------------- WALLET DATA --------------------- */
    private List<BalanceData> getBalances(List<WorkerData> wk) {
        List<String>          cr = tags("balance.wallet", "crypto");
        List<BalanceData> result = new ArrayList<>();
        cr.stream().forEach(c -> {
            result.addAll(getWalletBalances(c));
            wk.stream().forEach(v -> result.addAll(getPoolBalances(c, v.pool())));
        });
        return result;
    }
    private List<BalanceData> getWalletBalances(String crypto) {
        List<BalanceData> result = new ArrayList<>();

        Query query = BoundParameterQuery
                .QueryBuilder
                .newQuery("SELECT last(\"mining\") as \"lm\", last(\"holding\") as \"lh\" FROM " +
                        "\"" + influxRetentionPolicy + "\".\"balance.wallet\" " +
                        "WHERE time > now() - " + QUERY_PERIOD +" AND crypto = $crypto")
                .forDatabase(influxDatabaseName)
                .bind("crypto", crypto)
                .create();
        QueryResult results = influxDBTemplate.query(query);
        List<QueryResult.Series> sl = results.getResults().get(0).getSeries();

        if (null != sl) {
            result.add(
                    new BalanceData()
                            .crypto(Crypto.valueOf(crypto.toUpperCase()))
                            .source("wallet")
                            .type("mining")
                            .amount(Double.parseDouble(sl.get(0).getValues().get(0).get(1).toString()))
            );
            result.add(
                    new BalanceData()
                            .crypto(Crypto.valueOf(crypto.toUpperCase()))
                            .source("wallet")
                            .type("holding")
                            .amount(Double.parseDouble(sl.get(0).getValues().get(0).get(2).toString()))
            );
        }
        return result;
    }
    private List<BalanceData> getPoolBalances(String crypto, String pool) {
        List<BalanceData> result = new ArrayList<>();

        Query query = BoundParameterQuery
                .QueryBuilder
                .newQuery("SELECT last(\"confirmed\") as \"cb\", last(\"unconfirmed\") as \"ub\" FROM " +
                        "\"" + influxRetentionPolicy + "\".\"balance.pool\" " +
                        "WHERE time > now() - " + QUERY_PERIOD +" AND crypto = $crypto AND pool = $pool")
                .forDatabase(influxDatabaseName)
                .bind("crypto", crypto)
                .bind("pool", pool)
                .create();
        QueryResult results = influxDBTemplate.query(query);
        List<QueryResult.Series> sl = results.getResults().get(0).getSeries();

        if (null != sl) {
            result.add(
                    new BalanceData()
                            .crypto(Crypto.valueOf(crypto.toUpperCase()))
                            .source(pool)
                            .type("confirmed")
                            .amount(Double.parseDouble(sl.get(0).getValues().get(0).get(1).toString()))
            );
            result.add(
                    new BalanceData()
                            .crypto(Crypto.valueOf(crypto.toUpperCase()))
                            .source(pool)
                            .type("unconfirmed")
                            .amount(Double.parseDouble(sl.get(0).getValues().get(0).get(2).toString()))
            );
        }
        return result;
    }
}


//    @PostConstruct
//    public void init() {
//        influxDBTemplate.createDatabase();
//        influxDBTemplate.getRetentionPolicy();
//    }

//    private final Object syncObj = new Object();


/*

      workers : [
        {
          worker_name_1 : {
            rigs : [
              { name : "", cur: "", avg1d: "", avg1w : "", avg1m : ""},
                ...
            ],
            pools : [
              { name : "", cur: "", avg1d: "", avg1w : "", avg1m : ""},
                ...
            ]
          }
        }
      ],
      gpus : [
         rig_name: {
           worker_name : {
             gpu0: {
             }
           }
         }
      ]









        WORKER INFO:
        ----------------------------------------
        worker_name_1                                 // 1. подтянуть список активных worker'ов
                  rigXa | rigXb | rigXc | poolY1      // 2. для каждого worker'a подтянуть
        current:        |       |       |             //    список активных rig'ов и pool'ов
        1d avg :        |       |       |             // 3.
        1w avg :        |       |       |             //
        1m avg :        |       |       |             //
                                                      //
        worker_name_2
                  rigX1 | poolY2
        current:        |
        1d avg :        |
        7w avg :        |
        1m avg :        |
        ----------------------------------------



        GPU INFO (hashrate / power / temperature):
        ----------------------------------------      // 1. подтянуть список rig'ов
        rig_name_1                                    // 2. для каждого rig'а подтянуть
                 cur  |  1d  |  1w  |  1m             //    список GPU
        GPU 0         |      |      |                 // 3. для каждого GPU подтянуть
        GPU 1         |      |      |                 //    а) текущее значение
        ...                                           //    б) значение AVG 1
        GPU X         |      |      |                 //    в) значение AVG 2
        ----------------------------------------      //    г) значение AVG 3
        Total (pwr):



        BALANCE:
        ----------------------------------------
        Wallet:                                       // 1.
              RVN:    xxxxx [   yyyy]
              ZEC:   xxxxxx [     yy]
              ZEN:     xxxx [    yyy]
        Pool:
           pool_name:                  // <- for active workers only !
              RVN:       xx [     yy]
        ----------------------------------------                        */



/*
        public List<String> rigs() {
            Query query = new Query("SHOW TAG VALUES FROM \"rig.worker\" WITH KEY = \"host\"", influxDatabaseName);
            QueryResult res = influxDBTemplate.query(query);
            return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
        }
        public List<String> workers() {
            Query query = new Query("SHOW TAG VALUES FROM \"rig.worker\" WITH KEY = \"worker\"", influxDatabaseName);
            QueryResult res = influxDBTemplate.query(query);
            return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
        }
        public List<String> pools() {
            Query query = new Query("SHOW TAG VALUES FROM \"pool.worker\" WITH KEY = \"pool\"", influxDatabaseName);
            QueryResult res = influxDBTemplate.query(query);
            return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
        }
        public List<String> gpus() {
            Query query = new Query("SHOW TAG VALUES FROM \"rig.gpu\" WITH KEY = \"gpu\"", influxDatabaseName);
            QueryResult res = influxDBTemplate.query(query);
            return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
        }
        public List<String> cryptos() {
            Query query = new Query("SHOW TAG VALUES FROM \"balance.wallet\" WITH KEY = \"crypto\"", influxDatabaseName);
            QueryResult res = influxDBTemplate.query(query);
            return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
        }
*/



//    @Scheduled(fixedDelayString = "${schedule.timeout.agg}")
//    public void updateData() {
//        synchronized (aggregatedGPUData) {
//            StopWatch sw = new StopWatch();
//            sw.start();
//
//            aggregatedBalanceData = getBalances(aggregatedWorkerData);
//
//            periods.parallelStream().forEach(p -> {
//                String key = (p.equalsIgnoreCase(QUERY_PERIOD)) ? "cur" : p;
//                aggregatedWorkerData.parallelStream().forEach(wd -> {
//                    wd.rigRate(key, getAverageWorkerHashrate("rig", "host", wd.rig, wd.worker, p));
//                    wd.poolRate(key, getAverageWorkerHashrate("pool", "pool", wd.pool, wd.worker, p));
//                });
//                aggregatedGPUData.parallelStream().forEach(gd -> {
//                    GPUData ngd = getGPUData(gd.rig, gd.worker, gd.id + "", p);
//                    gd.hashrate(key, ngd.hashrate(p));
//                    gd.temperature(key, ngd.temperature(p));
//                    gd.power(key, ngd.power(p));
//                });
//            });
//
//            aggregatedWorkerData.stream().forEach(System.out::println);
//            aggregatedGPUData.stream().forEach(System.out::println);
//            aggregatedBalanceData.stream().forEach(System.out::println);
//
//            sw.stop();
//            System.out.println(sw.getTotalTimeMillis() / 1000.0);
//
//        }
//    }
