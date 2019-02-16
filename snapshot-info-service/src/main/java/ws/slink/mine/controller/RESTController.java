package ws.slink.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ws.slink.mine.aggregator.InformationAggregator;
import ws.slink.mine.model.BalanceData;
import ws.slink.mine.model.response.RigDataResponse;
import ws.slink.mine.model.response.WorkerDataResponse;

import java.util.List;

@Controller
@RequestMapping("/")
public class RESTController {

    @Autowired
    private InformationAggregator aggregator;

    @GetMapping(path="/worker", produces = "application/json")
    public @ResponseBody List<WorkerDataResponse> getWorkerData() {
        return aggregator.getWorkerData();
    }
    @GetMapping(path="/gpu", produces = "application/json")
    public @ResponseBody List<RigDataResponse> getGPUData() {
        return aggregator.getGPUData();
    }
    @GetMapping(path="/balance", produces = "application/json")
    public @ResponseBody List<BalanceData> getBalanceData() {
        return aggregator.getBalanceData();
    }

}
