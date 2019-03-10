package ws.slink.mine.model;

public class ServiceRequest {

    public String hmac;
    public String client;
    public String time;

    public ServiceRequest() {}

    public ServiceRequest hmac(String value) {
        this.hmac = value;
        return this;
    }
    public ServiceRequest client(String value) {
        this.client = value;
        return this;
    }
    public ServiceRequest time(String value) {
        this.time = value;
        return this;
    }

    public String toString() {
        return "HMAC: " + hmac + "; TIME: " + time + "; CLIENT: " + client;
    }

}
