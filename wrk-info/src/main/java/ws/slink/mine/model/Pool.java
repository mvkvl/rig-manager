package ws.slink.mine.model;

public enum Pool {
    SUPRNOVA("suprnova"),
    MINERMORE("minermore");

    private String value;
    Pool(String value) {
        this.value = value;
    }
    public String toString() {
        return this.value;
    }
}
