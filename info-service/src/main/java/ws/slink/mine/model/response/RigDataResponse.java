package ws.slink.mine.model.response;

import org.apache.commons.lang3.StringUtils;
import ws.slink.mine.model.GPUData;

import java.util.*;
import java.util.stream.Collectors;

public class RigDataResponse {

    private String rig;
    private Map<String, List<GPUData>> workers = new HashMap<>();

    public String getRig() {
        return rig;
    }
    public List<Worker> getWorkers() {
        return workers.keySet()
                      .stream()
                      .map(wk -> new Worker(wk, workers.get(wk)))
                      .collect(Collectors.toList());
    }

    public static RigDataResponse valueOf(GPUData gpuData) {
        RigDataResponse rigData = new RigDataResponse();
        rigData.rig = gpuData.rig();
        rigData.workers.put(gpuData.worker(), new ArrayList(Arrays.asList(gpuData)));
        return rigData;
    }

    public boolean add(GPUData gpuData) {
//        System.out.println(" >>>>> RIG: " + gpuData.rig() + "; WORKER: " + gpuData.worker());
        if (StringUtils.isBlank(gpuData.rig) || !gpuData.rig().equals(this.rig))
            return false;
        if (null != workers.get(gpuData.worker()))
            workers.get(gpuData.worker()).add(gpuData);
        else
            workers.put(gpuData.worker(), new ArrayList(Arrays.asList(gpuData)));
        return true;
    }

}
