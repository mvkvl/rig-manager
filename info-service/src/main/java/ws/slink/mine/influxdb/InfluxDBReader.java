package ws.slink.mine.influxdb;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class InfluxDBReader {

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Value("${spring.influxdb.database}")
    private String influxDatabaseName;

    @Value("${spring.influxdb.retention-policy}")
    private String influxRetentionPolicy;

    /* --------------- COMMON DATA --------------------- */
    public List<String> tags(String measurement, String key) {
        Query query = new Query("SHOW TAG VALUES FROM \"" + measurement +
                "\" WITH KEY = \"" + key + "\"", influxDatabaseName);
        QueryResult res = influxDBTemplate.query(query);
        return res.getResults().get(0).getSeries().get(0).getValues().stream().map(l -> l.get(1).toString()).collect(Collectors.toList());
    }

}







//    public QueryResult getPoint(Query query) {
//        QueryResult results = null;
//        int attempt = MAX_QUERY_ATTEMPTS;
//        while(attempt-- > 0) {
//            System.out.println(" >>> read attempt");
//            results = influxDBTemplate.query(query);
//            if (null == results.getError() && null != results.getResults().get(0).getSeries())
//                break;
//            try { Thread.sleep((MAX_QUERY_ATTEMPTS - attempt) * WAIT_TIME_BASE_SEC * 1000); }
//            catch (InterruptedException e) { e.printStackTrace(); }
//        }
//        return results;
//    }


//    @Autowired
//    private InformationAggregator aggregator;

//    @Scheduled(fixedDelay = 5000)
//    public void readW() {
//        System.out.println("--- workers ------------------------------------");
////        aggregator.getWorkerData().stream().forEach(System.out::println);
//        System.out.println(" # records: " + aggregator.getWorkerData().size());
//    }
//    @Scheduled(fixedDelay = 5000)
//    public void readG() {
//        System.out.println("--- gpu ----------------------------------------");
////        aggregator.getGPUData().stream().forEach(System.out::println);
//        System.out.println(" # records: " + aggregator.getGPUData().size());
//    }
//    @Scheduled(fixedDelay = 5000)
//    public void readB() {
//        System.out.println("--- balance ------------------------------------");
////        aggregator.getBalanceData().stream().forEach(System.out::println);
//        System.out.println(" # records: " + aggregator.getBalanceData().size());
//    }
