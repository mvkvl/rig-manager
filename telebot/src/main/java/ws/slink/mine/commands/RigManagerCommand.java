package ws.slink.mine.commands;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ws.slink.mine.model.ServiceRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Component
public class RigManagerCommand {

    private final String separator_str = "-------------------------------";

    @Value("${service.mgmt}")
    private String mgmtServiceURLStr;

    @Value("${keys.mgmt}")
    private String mgmtServiceKey;

    @Value("${mgmt-client.name}")
    private String clientName;

    @Autowired
    private RestTemplateBuilder restTemplate;

    public String stop(String worker) {
        StringBuilder sb = new StringBuilder();
        sb.append("Manager:\n")
          .append("<pre>")
          .append(separator_str).append("\n")
          .append(sendCommand("stop", worker)).append("\n")
          .append(separator_str)
          .append("</pre>");
        return sb.toString();
    }

    public String start(String worker) {
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>")
                .append("Manager:\n")
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
//        HttpEntity<ServiceRequest> request = new HttpEntity<>(createRequest());
        String res = restTemplate.build().postForObject(URLStr, createRequest(), String.class);
        return res;
    }

    private ServiceRequest createRequest() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeStr;
        timeStr = dateFormatGmt.format(new java.util.Date());
        ServiceRequest result = new ServiceRequest()
                                    .time(timeStr)
                                    .client(clientName)
                                    .hmac(getHMAC(timeStr));
        return result;
    }

    private String getHMAC(String timeStr) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, mgmtServiceKey)
                        .hmacHex(String.format("%s.%s", clientName, timeStr));
    }

}
