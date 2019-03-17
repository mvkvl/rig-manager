package ws.slink.mine.tools;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConfigTools {

    public static Map<String, Object> getPropertiesByPrefix(Environment env, String prefix) {
        Map<String, Object> rtn = new HashMap<>();
        if (env instanceof ConfigurableEnvironment) {
            for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
                if (propertySource instanceof EnumerablePropertySource) {
                    for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
                        if (key.startsWith(prefix))
                            rtn.put(key, propertySource.getProperty(key));
                    }
                }
            }
        }
        return rtn;
    }

    public static List<String> getPropertyPathComponent(Environment env, String prefix, int idx) {
        List<String> result = new ArrayList<>();
        getPropertiesByPrefix(env, "mgmt.crypto")
                .keySet().stream().forEach(s -> result.add(s.split("\\.")[idx]));
        return result;
    }
}
