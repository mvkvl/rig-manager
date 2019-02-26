package ws.slink.mine.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HostKeyServiceImpl implements HostKeyService {

    private static final Logger logger = LoggerFactory.getLogger(HostKeyServiceImpl.class);

    @Autowired
    private Environment env;

    Map<String, String> knownKeys  = new ConcurrentHashMap<>();

    public Optional<String> get(String host) {
        String result = knownKeys.get(host);
        if (StringUtils.isBlank(result)) {
            result = env.getProperty("monitoring.keys." + host);
            if (StringUtils.isNotBlank(result)) {
                knownKeys.put(host, result);
            }
        }
        return Optional.ofNullable(result);
    }

    public void clear() {
        knownKeys.clear();
    }

}
