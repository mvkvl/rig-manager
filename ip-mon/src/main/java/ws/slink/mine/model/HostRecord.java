package ws.slink.mine.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HostRecord {

    private String hostName;
    private String hostKey;
    private String ipAddress;
    private long   lastSeenTimestamp;

    public String getHostName() {
        return hostName;
    }
    public String getHostKey() {
        return hostKey;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public long getLastSeen() {
        return lastSeenTimestamp;
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

    public HostRecord updateTimestamp() {
        this.lastSeenTimestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        return this;
    }

    public static HostRecord valueOf(HostRecord record) {
        return new HostRecord()
                  .hostKey(record.getHostKey())
                  .hostName(record.getHostName())
                  .ipAddress(record.getIpAddress())
                  .updateTimestamp();
    }

    public String toString() {
        return new StringBuilder()
                .append(hostName)
                .append(" (")
                .append(ipAddress)
                .append("): ")
                .append(hostKey)
                .append(" [")
                .append(lastSeenTimestamp)
                .append("]")
                .toString();
    }

}
