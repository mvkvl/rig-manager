package ws.slink.mine.controller.response;

import ws.slink.mine.model.GPUData;

import java.util.List;
import java.util.stream.Collectors;

public class Worker {
    public String name;
    public List<GPU> gpus;
    public Worker(String name, List<GPUData> gpuData) {
        this.name = name;
        this.gpus = gpuData.stream().map(gd -> GPU.valueOf(gd)).collect(Collectors.toList());
    }
}
