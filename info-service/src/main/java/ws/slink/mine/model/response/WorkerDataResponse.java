package ws.slink.mine.model.response;

import org.apache.commons.lang3.StringUtils;
import ws.slink.mine.model.WorkerData;

import java.util.*;
import java.util.stream.Collectors;

public class WorkerDataResponse {

    private String worker;
    private Map<String, List<WorkerData>> rigs  = new HashMap<>();
    private Map<String, List<WorkerData>> pools = new HashMap<>();

    public String getWorker() {
        return worker;
    }
    public List<Rig> getRigs() {
        return rigs.keySet()
                   .stream()
                   .flatMap(rk -> rigs.get(rk).stream().map(r -> Rig.valueOf(r)))
                   .collect(Collectors.toList());
    }
    public List<Pool> getPools() {
        return pools.keySet()
                    .stream()
                    .flatMap(rk -> pools.get(rk).stream().map(v -> Pool.valueOf(v)))
                    .collect(Collectors.toList());
    }

    public static WorkerDataResponse valueOf(WorkerData data) {
        WorkerDataResponse wdata = new WorkerDataResponse();
        wdata.worker = data.worker();
        wdata.rigs.put(data.rig(), new ArrayList(Arrays.asList(data)));
        wdata.pools.put(data.pool(), new ArrayList(Arrays.asList(data)));
        return wdata;
    }

    public boolean add(WorkerData wdata) {
        if (StringUtils.isBlank(wdata.worker) || !wdata.rig().equals(this.worker))
            return false;
        if (null != rigs.get(wdata.rig()))
            rigs.get(wdata.rig()).add(wdata);
        else
            rigs.put(wdata.rig(), new ArrayList(Arrays.asList(wdata)));
        if (null != pools.get(wdata.pool()))
            pools.get(wdata.pool()).add(wdata);
        else
            pools.put(wdata.pool(), new ArrayList(Arrays.asList(wdata)));
        return true;
    }

}
