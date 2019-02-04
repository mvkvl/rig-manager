package ws.slink.mine.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import ws.slink.mine.influxdb.InfluxDBWriter;
import ws.slink.mine.model.NetworkInfo;
import ws.slink.mine.model.RigInfo;
import ws.slink.mine.model.WalletInfo;

import java.util.Arrays;
import java.util.List;

public class Receiver {

    @Autowired
    private InfluxDBWriter writer;

//    @RabbitListener(queues = "#{autoDeleteQueueWallet.name}")
//    public void receiveWallet(WalletInfo[] in) {
//        List<WalletInfo> wi = Arrays.asList(in);
//        writer.writeWalletInfo(wi);
//        receive(wi);
//    }

//    @RabbitListener(queues = "#{autoDeleteQueueNetwork.name}")
//    public void receiveNetwork(NetworkInfo[] in) {
//        List<NetworkInfo> ni = Arrays.asList(in);
//        writer.writeNetworkInfo(ni);
//        receive(ni);
//    }

    @RabbitListener(queues = "#{autoDeleteQueueRigInfo.name}")
    public void receiveNetwork(RigInfo[] in) {
        List<RigInfo> ri = Arrays.asList(in);
        writer.writeRigInfo(ri);
        receive(ri);
    }

    public void receive(List<? extends Object> in) {
        System.out.print("--- received info: ");
        in.stream().forEach(System.out::println);
    }

}
