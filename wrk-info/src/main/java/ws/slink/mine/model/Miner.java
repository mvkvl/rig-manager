package ws.slink.mine.model;

public enum Miner {
    TREX("trex"),
    ZENEMY("zenemy");

    private String value;
    Miner(String value) {
        this.value = value;
    }
    public String toString() {
        return this.value;
    }
}
