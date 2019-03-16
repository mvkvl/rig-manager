package ws.slink.mine.info.api.miner;

import ws.slink.mine.info.RigInfo;
import ws.slink.mine.info.model.MinerInfo;

public interface MinerAPI {
    RigInfo get(MinerInfo minerInfo);
}
