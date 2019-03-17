package ws.slink.mine.info.api.pool;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import ws.slink.mine.type.Pool;

@Component
public class PoolAPIFactory {

    private ObjectProvider<SuprnovaPoolAPI> suprnovaPoolAPIProvider;
    private ObjectProvider<MinermorePoolAPI> minermorePoolAPIProvider;

    public PoolAPIFactory(ObjectProvider<SuprnovaPoolAPI> suprnovaPoolAPIProvider,
                          ObjectProvider<MinermorePoolAPI> minermorePoolAPIProvider) {
        this.suprnovaPoolAPIProvider = suprnovaPoolAPIProvider;
        this.minermorePoolAPIProvider = minermorePoolAPIProvider;
    }

    public PoolAPI get(Pool pool) {
        switch(pool) {
            case SUPRNOVA:  return suprnovaPoolAPIProvider.getObject();
            case MINERMORE: return minermorePoolAPIProvider.getObject();
            default:
                throw new RuntimeException("unsupported pool: " + pool);
        }
    }

}
