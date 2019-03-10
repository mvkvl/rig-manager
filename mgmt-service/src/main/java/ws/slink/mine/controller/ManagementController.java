package ws.slink.mine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ws.slink.mine.manager.RigManager;

@Controller
@RequestMapping("/")
public class ManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    @Autowired private RigManager rigManager;

    @PostMapping(path="/stop", produces = "application/json")
    public @ResponseBody String stopMiners() {
        logger.trace("stop miner");
        rigManager.stop();
        return "stop command sent to workers";
    }

    @PostMapping(path="/stop/{miner}", produces = "application/json")
    public @ResponseBody String stopMiner(@PathVariable String miner) {
        logger.trace("stop miner ({})", miner);
        rigManager.stop(miner);
        return "stop command sent to " + miner;
    }

    @PostMapping(path="/start", produces = "application/json")
    public @ResponseBody String startMiners() {
        logger.trace("start miner");
        rigManager.start();
        return "start command sent to workers";
    }

    @PostMapping(path="/start/{miner}", produces = "application/json")
    public @ResponseBody String startMiner(@PathVariable String miner) {
        logger.trace("start miner ({})", miner);
        rigManager.start(miner);
        return "start command sent to " + miner;
    }

}
