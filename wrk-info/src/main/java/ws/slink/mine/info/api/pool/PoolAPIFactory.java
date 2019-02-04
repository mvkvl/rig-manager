package ws.slink.mine.info.api.pool;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.Pool;

@Component
public class PoolAPIFactory {

    private ObjectProvider<SuprnovaPoolAPI > suprnovaPoolAPIProvider;

    public PoolAPIFactory(ObjectProvider<SuprnovaPoolAPI> suprnovaPoolAPIProvider) {
        this.suprnovaPoolAPIProvider = suprnovaPoolAPIProvider;
    }

    public PoolAPI get(Pool pool) {
        switch(pool) {
            case SUPRNOVA: return suprnovaPoolAPIProvider.getObject();
//            case ZENEMY: return new ZenemyMinerAPI();
            default:
                throw new RuntimeException("unsupported pool: " + pool);
        }
    }

}
