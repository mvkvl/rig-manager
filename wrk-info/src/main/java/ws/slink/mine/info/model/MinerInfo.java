package ws.slink.mine.info.model;

import org.apache.commons.lang3.StringUtils;
import ws.slink.mine.type.Crypto;

/**
 *  Value-class for storing mine configuration information
 */
public class MinerInfo {
    public Crypto crypto;
    public String     pool;
    public String   worker;
    public String      rig;
    public String    miner;
    public String     host;
    public String     port;

    public MinerInfo crypto(Crypto value) {
        this.crypto = value;
        return this;
    }
    public MinerInfo pool(String value) {
        this.pool = value;
        return this;
    }
    public MinerInfo worker(String value) {
        this.worker = value;
        return this;
    }
    public MinerInfo rig(String value) {
        this.rig = value;
        return this;
    }
    public MinerInfo miner(String value) {
        this.miner = value;
        return this;
    }
    public MinerInfo host(String value) {
        this.host = value;
        return this;
    }
    public MinerInfo port(String value) {
        this.port = value;
        return this;
    }

    public boolean configured() {
        return null != crypto
                && StringUtils.isNotBlank(pool)
                && StringUtils.isNotBlank(worker)
                && StringUtils.isNotBlank(rig)
                && StringUtils.isNotBlank(miner)
                && StringUtils.isNotBlank(host)
                && StringUtils.isNotBlank(port);
    }

    public String toString() {
        return crypto  + "; "
             + pool    + "; "
             + worker  + "; "
             + rig     + "; "
             + miner   + "; "
             + host    + "; "
             + port;
    }
}
