package ws.slink.mine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ws.slink.mine.model.HostRecord;
import ws.slink.mine.model.HostUpdateResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class KnownHostsServiceImpl implements KnownHostsService {

    private static final Logger logger = LoggerFactory.getLogger(KnownHostsServiceImpl.class);

    private Map<String, HostRecord> knownHosts = new ConcurrentHashMap<>();

    @Value("${monitoring.timeout}")
    private long hostIpAddressTimeout;

    @Override public Optional<HostRecord> get(String hostName) {
        return Optional.ofNullable(knownHosts.get(hostName));
    }

    @Override public HostUpdateResult update(HostRecord message) {
        if (!knownHosts.containsKey(message.getHostName()))
            return processNewRecord(message);
        else
            return processExistingRecord(message);
    }
    @Override public Collection<String> checkHosts() {
        long ts = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Set<String> removedHosts = knownHosts
                .keySet()
                .stream()
                .filter(k -> (ts - knownHosts.get(k).getLastSeen() > hostIpAddressTimeout))
                .collect(Collectors.toSet());
        knownHosts.keySet().removeAll(removedHosts);
        return removedHosts;
    }

    private HostUpdateResult processNewRecord(HostRecord message) {
        knownHosts.put(message.getHostName(),
                       new HostRecord()
                        .hostName(message.getHostName())
                        .ipAddress(message.getIpAddress())
                        .hostKey("")
                        .updateTimestamp()
        );
        return HostUpdateResult.ONLINE;
    }
    private HostUpdateResult processExistingRecord(HostRecord message) {
        HostUpdateResult result = HostUpdateResult.UPDATE;
        HostRecord hr = knownHosts.get(message.getHostName());
        if (!hr.getIpAddress().equals(message.getIpAddress())) {
            hr.ipAddress(message.getIpAddress());
            result = HostUpdateResult.CHANGE;
        }
        hr.updateTimestamp();
        return result;
    }
}
