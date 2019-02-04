package ws.slink.mine.info.api.pool;

import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.PoolInfo;

public interface PoolAPI {
    public PoolInfo get(MinerInfo minerInfo);
}
