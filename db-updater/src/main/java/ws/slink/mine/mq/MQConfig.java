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

    @Value("${amqp.key.wallet:}")
    String wkey;

    @Value("${amqp.key.network:}")
    String nkey;

    @Value("${amqp.key.rig:}")
    String rkey;

    @Value("${amqp.key.pool:}")
    String pkey;

    @Value("${amqp.key.price:}")
    String pricekey;

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
    public Queue autoDeleteQueueWallet() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingWallet(TopicExchange topic,
                                 Queue autoDeleteQueueWallet) {
        return BindingBuilder.bind(autoDeleteQueueWallet)
                .to(topic)
                .with(wkey);
    }


    @Bean
    public Queue autoDeleteQueueNetwork() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingNetwork(TopicExchange topic,
                                  Queue autoDeleteQueueNetwork) {
        return BindingBuilder.bind(autoDeleteQueueNetwork)
                .to(topic)
                .with(nkey);
    }

    @Bean
    public Queue autoDeleteQueueRigInfo() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingRigInfo(TopicExchange topic,
                                  Queue autoDeleteQueueRigInfo) {
        return BindingBuilder.bind(autoDeleteQueueRigInfo)
                .to(topic)
                .with(rkey);
    }

    @Bean
    public Queue autoDeleteQueuePoolInfo() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingPoolInfo(TopicExchange topic,
                                   Queue autoDeleteQueuePoolInfo) {
        return BindingBuilder.bind(autoDeleteQueuePoolInfo)
                .to(topic)
                .with(pkey);
    }

    @Bean
    public Queue autoDeleteQueuePriceInfo() {
        return new AnonymousQueue();
    }
    @Bean
    public Binding bindingPriceInfo(TopicExchange topic,
                                    Queue autoDeleteQueuePriceInfo) {
        return BindingBuilder.bind(autoDeleteQueuePriceInfo)
                .to(topic)
                .with(pricekey);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

}
