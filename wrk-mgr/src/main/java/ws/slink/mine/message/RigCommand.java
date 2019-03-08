package ws.slink.mine.message;

import java.util.Map;

public class RigCommand {

    private String      command;
    private Map<String, String> arguments;

    public RigCommand() {}
    public RigCommand(String command, Map<String, String> arguments) {
        this.command   = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }
    public Map getArguments() {
        return arguments;
    }

    public String toString() {
        return "Rig Command: " + this.command + "; " + this.arguments;
    }
}
