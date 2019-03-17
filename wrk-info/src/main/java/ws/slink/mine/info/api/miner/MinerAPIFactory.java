package ws.slink.mine.info.api.miner;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import ws.slink.mine.type.Miner;

@Component
public class MinerAPIFactory {

    private ObjectProvider<TrexMinerAPI> trexMinerAPIProvider;

    public MinerAPIFactory(ObjectProvider<TrexMinerAPI> trexMinerAPIProvider) {
        this.trexMinerAPIProvider = trexMinerAPIProvider;
    }

    public MinerAPI get(Miner miner) {
        switch(miner) {
            case TREX: return trexMinerAPIProvider.getObject();
//            case ZENEMY: return new ZenemyMinerAPI();
            default:
                throw new RuntimeException("unsupported mine: " + miner);
        }
    }

}
