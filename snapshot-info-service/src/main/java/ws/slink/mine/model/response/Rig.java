package ws.slink.mine.model.response;

import ws.slink.mine.model.WorkerData;

import java.util.Map;

public class Rig {
    public String              name;
    public Map<String, Double> hashrate;
    public static Rig valueOf(WorkerData wdata) {
        Rig rig         = new Rig();
        rig.name        = wdata.rig();
        rig.hashrate    = wdata.rigHashrate;
        return rig;
    }
}
