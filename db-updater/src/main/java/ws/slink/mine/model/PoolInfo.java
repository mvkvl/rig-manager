package ws.slink.mine.model;

public class PoolInfo {
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
