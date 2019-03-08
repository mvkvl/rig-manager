package ws.slink.mine.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class RigManagerCommand {

    private final String separator_str = "---------------------------------";

    @Value("${service.mgmt}")
    private String mgmtServiceURLStr;

    @Autowired
    private RestTemplateBuilder restTemplate;

    public String stop(String worker) {
        StringBuilder sb = new StringBuilder();

        sb.append("Manager:")
          .append("<pre>")
          .append(separator_str).append("\n")
          .append(sendCommand("stop", worker)).append("\n")
          .append(separator_str)
          .append("</pre>");

        return sb.toString();
    }

    public String start(String worker) {
        StringBuilder sb = new StringBuilder();

        sb.append("Manager:")
                .append("<pre>")
                .append(separator_str).append("\n")
                .append(sendCommand("start", worker)).append("\n")
                .append(separator_str)
                .append("</pre>");

        return sb.toString();
    }

    private String sendCommand(String pathParam, String arg) {
        URL url;
        String URLStr = mgmtServiceURLStr + "/" + pathParam;
        URLStr = (null != arg) ? URLStr + "/"  + arg : URLStr;
        try {
            url = new URL(URLStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed url exception: " + URLStr);
        }
        String res = restTemplate.build().postForObject(URLStr, null, String.class);
        return res;
    }

}
