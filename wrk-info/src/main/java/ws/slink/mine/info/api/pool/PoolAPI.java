package ws.slink.mine.info.api.pool;

import ws.slink.mine.info.conf.MinerInfo;
import ws.slink.mine.info.model.PoolInfo;

public interface PoolAPI {
    PoolInfo get(MinerInfo minerInfo);
}
