package ws.slink.mine.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ws.slink.mine.model.NetworkInfo;
import ws.slink.mine.model.WalletInfo;

import java.util.List;

public class Receiver {

    @RabbitListener(queues = "#{autoDeleteQueueWallet.name}")
    public void receiveWallet(List<WalletInfo> in) {
        System.out.println("received wallet info: ");
        receive(in);
    }

    @RabbitListener(queues = "#{autoDeleteQueueNetwork.name}")
    public void receiveNetwork(List<NetworkInfo> in) {
        System.out.println("received network info: ");
        receive(in);
    }

    public void receive(List<? extends Object> in) {
//        System.out.println(" [x] Received '" + in + "'");
        in.stream().forEach(System.out::println);
        System.out.println("------------------------------------------------");
    }

}
