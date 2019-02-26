package ws.slink.mine.service;

import java.util.Optional;

public interface HostKeyService {

    Optional<String> get(String hostName);
    void clear();

}
