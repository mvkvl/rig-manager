package ws.slink.mine.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.slink.mine.message.RigCommand;
import ws.slink.notifier.TelegramNotifierBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Value("${miner.script:}")
    private String minerScript;

    public void process(RigCommand command) {
        switch(command.getCommand().toUpperCase()) {
            case "START":
                start(command);
                break;
            case "STOP":
                stop(command);
                break;
            default:
                break;
        }
    }

    private void start(RigCommand command) {
        String worker = "";
        if (null != command.getArguments()    &&
            !command.getArguments().isEmpty() &&
             command.getArguments().containsKey("miner")) {
            worker = (String)command.getArguments().get("miner");
        }
        runCommand(Arrays.asList(minerScript, "start", worker));
    }
    private void stop(RigCommand command) {
        String worker = "";
        if (null != command.getArguments()    &&
                !command.getArguments().isEmpty() &&
                command.getArguments().containsKey("miner")) {
            worker = (String)command.getArguments().get("miner");
        }
        runCommand(Arrays.asList(minerScript, "stop", worker));
    }

    private void runCommand(List<String> args) {
        try {
            Process p = new ProcessBuilder(args).start();
            try (BufferedReader read = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                p.waitFor();
                StringBuilder sb = new StringBuilder(" <b>wrk-mgr</b>: ");
                while (read.ready()) {
                    String s = read.readLine();
                    sb.append(s);
                    logger.trace("process output: {}", s);
                }
                telegramNotifier.sendMessage(sb.toString());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
