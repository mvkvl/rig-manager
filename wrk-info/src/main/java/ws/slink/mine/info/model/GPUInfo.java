package ws.slink.mine.info.model;

/**
 *
 * Rig/GPU information query result
 *
 */
public class GPUInfo {

    public GPUInfo() {
    }

    public GPUInfo id(int value) {
        this.id = value;
        return this;
    }
    public GPUInfo hashrate(double value) {
        this.hashrate = value;
        return this;
    }
    public GPUInfo temperature(double value) {
        this.temperature = value;
        return this;
    }
    public GPUInfo power(double value) {
        this.power = value;
        return this;
    }
    public GPUInfo calculate() {
        efficiency = (power > 0) ? hashrate / power : 0;
        return this;
    }

    public int id() {
        return this.id;
    }
    public double hashrate() {
        return hashrate;
    }
    public double temperature() {
        return temperature;
    }
    public double power() {
        return power ;
    }
    public double efficiency() {
        return efficiency;
    }

    public String toString() {
        return  ""   + id() +
                ", " + hashrate() +
                ", " + temperature() +
                ", " + power() +
                ", " + efficiency();
    }

    public int             id;
    public double    hashrate;
    public double temperature;
    public double       power;
    public double  efficiency;

}
