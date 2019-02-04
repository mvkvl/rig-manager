package ws.slink.mine.model;

public class GPUInfo {
    public int             id;
    public double    hashrate;
    public double temperature;
    public double       power;
    public double  efficiency;

    public String toString() {
        return  "" + id +
                ", " + hashrate +
                ", " + temperature +
                ", " + power +
                ", " + efficiency;
    }

}
