package ws.slink.mine.model.response;

import ws.slink.mine.model.GPUData;

import java.util.Map;

public class GPU {
    public int                 id = -1;
    public Map<String, Double> hashrate;
    public Map<String, Double> temperature;
    public Map<String, Double> power;

    public static GPU valueOf(GPUData gdata) {
        GPU gpu         = new GPU();
        gpu.id          = gdata.id;
        gpu.hashrate    = gdata.hashrate;
        gpu.temperature = gdata.temperature;
        gpu.power       = gdata.power;
        return gpu;
    }

}
