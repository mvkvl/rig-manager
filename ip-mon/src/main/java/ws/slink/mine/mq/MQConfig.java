package ws.slink.mine.mq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Configuration
public class MQConfig {

    @Value("${amqp.url:}")
    String uri;

    @Value("${amqp.key.ipmon:}")
    String imkey;

    @Bean
    public NamingStrategy namingStrategy() {
        return () -> "mine." + UUID.randomUUID().toString();
    }

    @Bean
    public ConnectionFactory mqConnectionFactory() {
        URI url = null;
        try {
            url = new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace(); // should not happen
        }

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(url.getHost());
        connectionFactory.setUsername(url.getUserInfo().split(":")[0]);
        connectionFactory.setPassword(url.getUserInfo().split(":")[1]);
        if (StringUtils.isNotBlank(url.getPath()))
            connectionFactory.setVirtualHost(url.getPath().replace("/", ""));
        connectionFactory.setConnectionTimeout(3000);
        connectionFactory.setRequestedHeartBeat(30);

        return connectionFactory;
    }

    @Bean
    public TopicExchange topic() { return new TopicExchange("mine.topic"); }

    @Bean
    public Queue autoDeleteQueueIpMonitor() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingIpMonitor(TopicExchange topic,
                                    Queue autoDeleteQueueIpMonitor) {
        return BindingBuilder.bind(autoDeleteQueueIpMonitor)
                             .to(topic)
                             .with(imkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
