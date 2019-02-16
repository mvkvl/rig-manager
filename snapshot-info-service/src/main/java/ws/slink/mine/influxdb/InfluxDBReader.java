package ws.slink.mine.influxdb;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class InfluxDBReader {


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



}
