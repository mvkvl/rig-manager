package ws.slink.mine.info.model;

import ws.slink.mine.type.Crypto;

/**
 * Blockchain network information query result
 */
public class PoolInfo {

    public PoolInfo crypto(Crypto value) {
        this.crypto = value;
        return this;
    }
    public PoolInfo pool(String value) {
        this.pool = value;
        return this;
    }
    public PoolInfo worker(String value) {
        this.worker = value;
        return this;
    }
    public PoolInfo hashrate(double value) {
        this.hashrate = value;
        return this;
    }
    public PoolInfo average(double value) {
        this.average = value;
        return this;
    }
    public PoolInfo confirmed(double value) {
        this.confirmed = value;
        return this;
    }
    public PoolInfo unconfirmed(double value) {
        this.unconfirmed = value;
        return this;
    }

    public String toString() {
        return    crypto     + ", "
                + pool       + ", "
                + worker     + ", "
                + hashrate   + ", "
                + average    + ", "
                + confirmed  + ", "
                + unconfirmed;
    }

    public Crypto crypto;
    public String pool;
    public String worker;
    public double hashrate;
    public double average;
    public double confirmed;
    public double unconfirmed;
}
