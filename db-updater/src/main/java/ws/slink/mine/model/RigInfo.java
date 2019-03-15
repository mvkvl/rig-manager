package ws.slink.mine.model;

import ws.slink.mine.type.Crypto;

import java.util.List;

public class RigInfo {

    public Crypto crypto;
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

    public String toString() {
        return    crypto     + ", "
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

}
