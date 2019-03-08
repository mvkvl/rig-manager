package ws.slink.mine.manager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.RigMessage;
import ws.slink.mine.mq.Sender;

import java.util.HashMap;
import java.util.Map;

@Component
public class RigManagerImpl implements RigManager {

    private static final Logger logger = LoggerFactory.getLogger(RigManagerImpl.class);

    @Autowired
    private Sender sender;

    @Value("${amqp.key.rig}") private String amqpManagementKey;

    @Value("${keys.manager}") private String managerKey;

    @Override public RigMessage stop() {
        return stop("");
    }
    @Override public RigMessage stop(String miner) {
        return rig_command("STOP", miner);
    }
    @Override public RigMessage start() {
        return start("");
    }
    @Override public RigMessage start(String miner) {
        return rig_command("START", miner);
    }

    private RigMessage rig_command(String command, String miner) {
        return rig_command(command, miner, null);
    }

    private RigMessage rig_command(String command, String miner, String args) {
        logger.trace("sending rig command {}({}, {})", command, miner, args);
        Map<String, String> arguments = new HashMap<>();
        if (StringUtils.isNotBlank(miner)) arguments.put("miner", miner);
        if (StringUtils.isNotBlank(args))  arguments.put("args", args);
        RigMessage msg = new RigMessage(command, arguments, managerKey);
        sender.send(amqpManagementKey, msg);
        return msg;
    }

}
