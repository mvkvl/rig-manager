package ws.slink.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ws.slink.mine.aggregator.InformationAggregator;
import ws.slink.mine.controller.response.DataResponseBalance;
import ws.slink.mine.controller.response.DataResponseRig;
import ws.slink.mine.controller.response.DataResponseWorker;

import java.util.List;

@Controller
@RequestMapping("/")
public class RESTController {

    @Autowired
    private InformationAggregator aggregator;

    @GetMapping(path="/worker", produces = "application/json")
    public @ResponseBody List<DataResponseWorker> getWorkerData() {
        return aggregator.getWorkerData();
    }
    @GetMapping(path="/gpu", produces = "application/json")
    public @ResponseBody List<DataResponseRig> getGPUData() {
        return aggregator.getGPUData();
    }
    @GetMapping(path="/balance", produces = "application/json")
    public @ResponseBody List<DataResponseBalance> getBalanceData() {
        return aggregator.getBalanceData();
    }

}
