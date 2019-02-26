package ws.slink.mine.monitor;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.net.util.IPAddressUtil;
import ws.slink.mine.mq.Sender;

@Component
@EnableScheduling
public class Updater {

    private static final Logger logger = LoggerFactory.getLogger(Updater.class);
    private static final String IP_CHECK_URL = "http://ipinfo.io/ip";

    volatile private String currentIpAddress = "";

    @Value("${monitoring.hostname}")
    private String hostName;

    @Value("${monitoring.hostkey}")
    private String hostKey;

    @Value("${amqp.key.ipmon}")
    private String amqpMonitorKey;

    @Autowired private RestTemplateBuilder restTemplateBuilder;
    @Autowired private Sender sender;

    @Scheduled(initialDelay = 1000, fixedRateString = "${schedule.timeout.ping}")
    private void ping() {
        String hmac =
            new HmacUtils(HmacAlgorithms.HMAC_SHA_256, hostKey)
                    .hmacHex(hostName + "." + currentIpAddress);
        HostRecord hr = new HostRecord()
                .hostKey(hmac)
                .hostName(hostName)
                .ipAddress(currentIpAddress);

        sender.send(amqpMonitorKey, hr);

        logger.trace("monitoring client ping: " + hr);
    }

    @Scheduled(initialDelay = 0, fixedRateString = "${schedule.timeout.ipcheck}")
    private void checkIp() {
        String res = restTemplateBuilder.build().getForObject(IP_CHECK_URL, String.class);
        // TODO: null / error(?) check should be done here
        String ip = res.trim();
        if (IPAddressUtil.isIPv4LiteralAddress(ip)) {
            synchronized (this) {
                currentIpAddress = ip;
            }
            logger.trace("monitoring client ip check [current IP address]: " + ip);
        }
        else {
            logger.warn("monitoring client ip check [not an IP address]: " + ip);
        }
    }

}
