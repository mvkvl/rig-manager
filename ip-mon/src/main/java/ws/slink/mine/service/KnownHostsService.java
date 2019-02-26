package ws.slink.mine.service;

import ws.slink.mine.model.HostRecord;
import ws.slink.mine.model.HostUpdateResult;

import java.util.Collection;
import java.util.Optional;

public interface KnownHostsService {

    Optional<HostRecord> get(String hostName);
    HostUpdateResult update(HostRecord message);
    Collection<String> checkHosts();

}
