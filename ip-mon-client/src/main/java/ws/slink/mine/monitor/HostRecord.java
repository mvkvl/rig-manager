package ws.slink.mine.monitor;

public class HostRecord {

    private String hostName;
    private String hostKey;
    private String ipAddress;

    public String getHostName() {
        return hostName;
    }
    public String getHostKey() {
        return hostKey;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public HostRecord hostName(String value) {
        this.hostName = value;
        return this;
    }
    public HostRecord hostKey(String value) {
        this.hostKey = value;
        return this;
    }
    public HostRecord ipAddress(String value) {
        this.ipAddress = value;
        return this;
    }

    public String toString() {
        return new StringBuilder()
                .append(hostName)
                .append(" (")
                .append(ipAddress)
                .append("): ")
                .append(hostKey)
                .toString();
    }

}
