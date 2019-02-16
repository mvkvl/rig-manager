package ws.slink.mine.model;

import java.util.HashMap;
import java.util.Map;

public class WorkerData {
    public String worker;
    public String rig;
    public String pool;
    public String crypto;
    public Map<String, Double> rigHashrate  = new HashMap<>();
    public Map<String, Double> poolHashrate = new HashMap<>();

    public WorkerData worker(String value) {
        this.worker = value;
        return this;
    }
    public WorkerData rig(String value) {
        this.rig = value;
        return this;
    }
    public WorkerData pool(String value) {
        this.pool = value;
        return this;
    }
    public WorkerData crypto(String value) {
        this.crypto = value;
        return this;
    }
    public WorkerData rigRate(String key, double value) {
        this.rigHashrate.put(key, value);
        return this;
    }
    public WorkerData poolRate(String key, double value) {
        this.poolHashrate.put(key, value);
        return this;
    }

    public String worker() {
        return worker;
    }
    public String rig() {
        return rig;
    }
    public String pool() {
        return pool;
    }
    public String crypto() {
        return crypto;
    }
    public double rigRate(String key) {
        return (rigHashrate.containsKey(key)) ? rigHashrate.get(key) : 0.0;
    }
    public double poolRate(String key) {
        return (poolHashrate.containsKey(key)) ? poolHashrate.get(key) : 0.0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(worker)
                .append(":")
                .append(rig)
                .append(".")
                .append(pool)
                .append("  RIG: ").append(rigHashrate.toString())
                .append(" POOL: ").append(poolHashrate.toString());
        return sb.toString();
    }
}