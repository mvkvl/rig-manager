package ws.slink.mine.info.api.miner;

import ws.slink.mine.info.model.MinerInfo;
import ws.slink.mine.info.model.RigInfo;

public interface MinerAPI {
    RigInfo get(MinerInfo minerInfo);
}
