package ws.slink.mine.model;

import java.util.Map;

public class RigMessage {

    private String      command;
    private Map<String, String> arguments;
    private String      key;

    public RigMessage(String command, Map<String, String> arguments, String key) {
        this.command   = command;
        this.arguments = arguments;
        this.key       = key;
    }

    public String getCommand() {
        return command;
    }
    public Map getArguments() {
        return arguments;
    }

//    public String hmac() {
//        String hmac =
//                new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)
//                        .hmacHex(hostName + "." + currentIpAddress);
//
//        return null;
//    }

}
