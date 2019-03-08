package ws.slink.mine.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import ws.slink.mine.manager.CommandProcessor;
import ws.slink.mine.message.RigCommand;

public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private CommandProcessor commandProcessor;

    @RabbitListener(queues = "#{autoDeleteQueueRigCommand.name}")
    public void receiveWalletInfo(RigCommand in) {
        commandProcessor.process(in);
        receive(in);
    }

    public void receive(RigCommand in) {
        logger.debug("--- received info: " + in);
//        in.stream().forEach(v -> logger.debug(v.toString()));
    }

}
