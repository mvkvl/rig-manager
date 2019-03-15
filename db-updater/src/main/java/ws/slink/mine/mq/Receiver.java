package ws.slink.mine.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import ws.slink.mine.influxdb.InfluxDBWriter;
import ws.slink.mine.model.*;

import java.util.Arrays;
import java.util.List;

public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private InfluxDBWriter writer;

    @RabbitListener(queues = "#{autoDeleteQueueWallet.name}")
    public void receiveWalletInfo(WalletInfo[] in) {
        List<WalletInfo> info = Arrays.asList(in);
        writer.writeWalletInfo(info);
        receive(info);
    }

    @RabbitListener(queues = "#{autoDeleteQueueNetwork.name}")
    public void receiveNetworkInfo(NetworkInfo[] in) {
        List<NetworkInfo> info = Arrays.asList(in);
        writer.writeNetworkInfo(info);
        receive(info);
    }

    @RabbitListener(queues = "#{autoDeleteQueueRigInfo.name}")
    public void receiveRigInfo(RigInfo[] in) {
        List<RigInfo> info = Arrays.asList(in);
        writer.writeRigInfo(info);
        receive(info);
    }

    @RabbitListener(queues = "#{autoDeleteQueuePoolInfo.name}")
    public void receivePoolInfo(PoolInfo[] in) {
        List<PoolInfo> info = Arrays.asList(in);
        writer.writePoolInfo(info);
        receive(info);
    }

    @RabbitListener(queues = "#{autoDeleteQueuePriceInfo.name}")
    public void receivePriceInfo(PriceInfo[] in) {
        List<PriceInfo> info = Arrays.asList(in);
        writer.writePriceInfo(info);
        receive(info);
    }

    public void receive(List<? extends Object> in) {
        logger.debug("--- received info: ");
        in.stream().forEach(v -> logger.debug(v.toString()));
    }

}
