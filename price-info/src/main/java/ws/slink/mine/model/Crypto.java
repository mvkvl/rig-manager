package ws.slink.mine.model;

public enum Crypto {
    RVN("rvn"),
    ZEC("zec"),
    ZEN("zen"),
    XMR("xmr"),
    XLM("xlm"),
    BTC("btc");

    private String value;
    Crypto(String value) {
        this.value = value;
    }
    public String toString() {
        return this.value;
    }
}
