package ws.slink.mine.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange topic;

//    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send(String key, Object object) {
        template.convertAndSend(topic.getName(), key, object);
        try {
            ObjectMapper om = new ObjectMapper();
            logger.trace("Sent '" + om.writeValueAsString(object) + "' to '" + topic.getName() + "' with key '" + key + "'");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
