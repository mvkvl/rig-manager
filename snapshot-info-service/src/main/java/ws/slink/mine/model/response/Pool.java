package ws.slink.mine.model.response;

import ws.slink.mine.model.WorkerData;

import java.util.Map;

public class Pool {
    public String name;
    public Map<String, Double> hashrate;
    public static Pool valueOf(WorkerData wdata) {
        Pool pool     = new Pool();
        pool.name     = wdata.pool();
        pool.hashrate = wdata.poolHashrate;
        return pool;
    }
}
