package ws.slink.mine.influxdb;

import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ws.slink.mine.aggregator.InformationAggregator;
import ws.slink.mine.model.GPUData;
import ws.slink.mine.model.WorkerData;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class InfluxDBReader {


    @Autowired
    private InformationAggregator aggregator;

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
