package ws.slink.mine.monitor;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.HostRecord;
import ws.slink.mine.service.HostKeyService;
import ws.slink.mine.service.KnownHostsService;
import ws.slink.mine.service.NotificationService;

import java.util.Optional;

@Component
@EnableScheduling
public class IpMonitor {

    private static final Logger logger = LoggerFactory.getLogger(IpMonitor.class);

    @Autowired private HostKeyService      hostKeyService;
    @Autowired private KnownHostsService   knownHostsService;
    @Autowired private NotificationService notificationService;

    @RabbitListener(queues = "#{autoDeleteQueueIpMonitor.name}")
    public void receiveHostUpdate(HostRecord message) {
        if (isVerifiedUpdate(message)) {
            logger.trace("Received verified message: {}", message);
            switch (knownHostsService.update(message)) {
                case ONLINE:
                    notificationService.notify("<b>"                 +
                                               message.getHostName() +
                                               "</b>: online",
                                               0x0001F4AB
                                              );
                    break;
                case CHANGE:
                    notificationService.notify("<b>"                 +
                                               message.getHostName() +
                                               "</b>: new address "  +
                                               message.getIpAddress(),
                                               0x000026A1
                                              );
                    break;
                default:
                    break;
            }
        } else {
            logger.warn("message not signed or sender is unknown");
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 60000)
    private void checkHosts() {
        logger.trace("checking hosts");
        knownHostsService.checkHosts()
                .stream()
                .forEach(h ->
                        notificationService.notify("<b>" + h + "</b>: offline", 0x0001F4A5));
    }

    private boolean isVerifiedUpdate(HostRecord message) {
        Optional<String> hostKey = hostKeyService.get(message.getHostName());
        if (hostKey.isPresent()) {
            return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, hostKey.get())
                    .hmacHex(message.getHostName() + "." + message.getIpAddress())
                    .equals(message.getHostKey());
        } else {
            return false;
        }
    }

    @Scheduled(fixedRate = 86400000)
    private void cleanHostKeys() {
        logger.trace("cleaning up host keys");
        hostKeyService.clear();
    }
}
