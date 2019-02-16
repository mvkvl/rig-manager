package ws.slink.mine.model;

import java.util.HashMap;
import java.util.Map;

public class GPUData {

    public int                 id     = -1;
    public String              rig    = "";
    public String              worker = "";
    public Map<String, Double> hashrate;
    public Map<String, Double> temperature;
    public Map<String, Double> power;

    public GPUData() {
        hashrate    = new HashMap<>();
        temperature = new HashMap<>();
        power       = new HashMap<>();
    }
    public GPUData id(int value) {
        this.id = value;
        return this;
    }
    public GPUData rig(String value) {
        this.rig = value;
        return this;
    }
    public GPUData worker(String value) {
        this.worker = value;
        return this;
    }
    public GPUData hashrate(String key, double value) {
        this.hashrate.put(key, value);
        return this;
    }
    public GPUData temperature(String key, double value) {
        this.temperature.put(key, value);
        return this;
    }
    public GPUData power(String key, double value) {
        this.power.put(key, value);
        return this;
    }

    public int id() {
        return id;
    }
    public String rig() {
        return rig;
    }
    public String worker() {
        return worker;
    }
    public double temperature(String key) {
        return (temperature.containsKey(key)) ? temperature.get(key) : 0.0;
    }
    public double hashrate(String key) {
        return (hashrate.containsKey(key)) ? hashrate.get(key) : 0.0;
    }
    public double power(String key) {
        return (power.containsKey(key)) ? power.get(key) : 0.0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rig).append(".").append(worker).append(".GPU").append(id).append("\t")
          .append("  HR: ").append(hashrate.toString())
          .append("  TM: ").append(temperature.toString())
          .append("  PW: ").append(power.toString());
        return sb.toString();
    }
}
