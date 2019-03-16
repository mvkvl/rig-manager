package ws.slink.mine.info.api.pool;

import ws.slink.mine.info.PoolInfo;
import ws.slink.mine.info.model.MinerInfo;

public interface PoolAPI {
    PoolInfo get(MinerInfo minerInfo);
}
