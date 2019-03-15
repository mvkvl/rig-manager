package ws.slink.mine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigParams {

    @Autowired
    private Environment env;

    public String get(String configKey) {
        return env.getProperty(configKey);
    }

}
