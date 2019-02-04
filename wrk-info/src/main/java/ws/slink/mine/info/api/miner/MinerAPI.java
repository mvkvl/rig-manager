package ws.slink.mine.info.api.miner;

import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.RigInfo;

public interface MinerAPI {
    public RigInfo get(MinerInfo minerInfo);
}
