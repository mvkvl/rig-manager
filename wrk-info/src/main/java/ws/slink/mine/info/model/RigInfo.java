package ws.slink.mine.info.model;

import ws.slink.mine.model.Crypto;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Rig information query result
 *
 */
public class RigInfo {

    public RigInfo() {
    }
    public RigInfo crypto(Crypto value) {
        this.crypto = value;
        return this;
    }
    public RigInfo pool(String value) {
        this.pool = value;
        return this;
    }
    public RigInfo worker(String value) {
        this.worker = value;
        return this;
    }
    public RigInfo rig(String value) {
        this.rig = value;
        return this;
    }
    public RigInfo name(String value) {
        this.name = value;
        return this;
    }
    public RigInfo accepted(int value) {
        this.accepted = value;
        return this;
    }
    public RigInfo rejected(int value) {
        this.rejected = value;
        return this;
    }
    public RigInfo gpus(List<GPUInfo> gpus) {
        this.gpus = gpus;
        return this;
    }

    public RigInfo calculate() {
        if (null != gpus && !gpus.isEmpty()) {
            totalHash = gpus.stream().collect(Collectors.summingDouble(GPUInfo::hashrate));
            totalPower = gpus.stream().collect(Collectors.summingDouble(GPUInfo::power));
            totalEff = (totalPower > 0) ? totalHash / totalPower : 0;
        }
        return this;
    }

    public String toString() {

        return crypto     + ", "
             + pool       + ", "
             + worker     + ", "
             + rig        + ", "
             + name       + ", "
             + totalHash  + ", "
             + totalPower + ", "
             + totalEff   + ", "
             + accepted   + ", "
             + rejected   + ", "
             + gpus;
    }

    public Crypto      crypto;
    public String        pool;
    public String      worker;
    public String         rig;
    public String        name;
    public double   totalHash;
    public double  totalPower;
    public double    totalEff;
    public int       accepted;
    public int       rejected;
    public List<GPUInfo> gpus;

}
