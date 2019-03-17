package ws.slink.mine.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    @Autowired private RabbitTemplate template;

    @Autowired private TopicExchange topic;

    public void send(String key, List<? extends Object> data) {
        logger.trace("sending " + data);
        template.convertAndSend(topic.getName(), key, data);
        try {
            ObjectMapper om = new ObjectMapper();
//            om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            logger.trace("Sent '" + om.writeValueAsString(data) + "' to '" + topic.getName() + "' with key '" + key + "'");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void send(String key, Object data) {
        logger.trace("sending " + data);
        template.convertAndSend(topic.getName(), key, data);
        try {
            ObjectMapper om = new ObjectMapper();
//            om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            logger.trace("Sent '" + om.writeValueAsString(data) + "' to '" + topic.getName() + "' with key '" + key + "'");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
