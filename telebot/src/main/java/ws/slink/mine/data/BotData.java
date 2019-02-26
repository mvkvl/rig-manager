package ws.slink.mine.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotData {

    @Autowired
    private RestTemplateBuilder restTemplate;

    @Value("${data.url}")
    private String infoServiceURLStr;

    private final String separator_str = "-------------------------------";

    public String getBalanceData() {
        List<Map> balances = getJsonData("balance");
        String formatStr = "  %5s:%11.2f [%8.2f]\n";
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("Balances:\n").append(separator_str).append("\nwallets:\n");
        balances.stream()
                .filter(j -> j.get("source").toString().equalsIgnoreCase("wallet"))
                .sorted((a, b) -> a.get("crypto").toString().compareTo(b.get("crypto").toString()))
                .forEach(v -> sb.append("")
                                .append(String.format(formatStr,
                                        v.get("crypto").toString().toLowerCase(),
                                        String.format("%,f", Double.parseDouble(v.get("holding").toString())).replace(",", " "),
                                        String.format("%,f", Double.parseDouble(v.get("mining").toString())).replace(",", " ")
                                )));
        sb.append("pools:\n");
        List<String> pools = new ArrayList<>(
                balances.stream()
                        .filter(j -> !j.get("source").toString().equalsIgnoreCase("wallet"))
                        .map(v -> v.get("source").toString()).collect(Collectors.toSet()));
        pools.stream().forEach(p -> {
            sb.append("  ").append(p).append(":\n");
            balances.stream()
                    .filter(b -> b.get("source").toString().equalsIgnoreCase(p))
                    .sorted((a, b) -> a.get("crypto").toString().compareTo(b.get("crypto").toString()))
                    .forEach(b -> {
                        sb.append("")
                          .append(String.format(formatStr,
                                  b.get("crypto").toString().toLowerCase(),
                                  Double.parseDouble(b.get("confirmed").toString()),
                                  Double.parseDouble(b.get("unconfirmed").toString())));
            });
        });
        sb.append(separator_str).append("</pre>");
        return sb.toString();
    }
    public String getWorkerData() {
        String formatStr = "%10s  %3s  %3s  %3s  %3s\n";
        List<Map> wd = getJsonData("worker");
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("Workers:\n").append(separator_str).append("\n");
        List<String> workers = new ArrayList<>(
                      wd.stream()
                        .sorted((a, b) -> a.get("worker").toString().compareTo(b.get("worker").toString()))
                        .map(v -> v.get("worker").toString()).collect(Collectors.toSet()));
        workers.stream().forEach(v -> {
             sb.append(v).append(":\n")
               .append(String.format(formatStr, "", "cur", "1d", "1w", "1m"));
             wd.stream()
               .filter(w -> w.get("worker").toString().equalsIgnoreCase(v))
               .forEach(w -> {
                   processWorkerRigs (sb, w.get("rigs"));
                   processWorkerPools(sb, w.get("pools"));
               });
        });
        sb.append(separator_str).append("</pre>");
        return sb.toString();
    }
    public String getGPUHashrateData() {
        List<Map> gpus = getJsonData("gpu");
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("GPU Hashrate:\n").append(separator_str).append("\n");
        processGPUInfo(sb, gpus, "hashrate");
        sb.append(separator_str).append("</pre>");
        return sb.toString();
    }
    public String getGPUTemperatureData() {
        List<Map> gpus = getJsonData("gpu");
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("GPU Temperature:\n").append(separator_str).append("\n");
        processGPUInfo(sb, gpus, "temperature");
        sb.append(separator_str).append("</pre>");
        return sb.toString();
    }
    public String getGPUPowerData() {
        List<Map> gpus = getJsonData("gpu");
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("GPU Power:\n").append(separator_str).append("\n");
        processGPUInfo(sb, gpus, "power");
        sb.append(separator_str).append("</pre>");
        return sb.toString();
    }

    private void processWorkerRigs(StringBuilder sb, Object obj) {
        String formatStr = "%10s: %3d  %3d  %3d  %3d\n";
        List<Map> list = (List<Map>)obj;
        list.stream()
                .forEach(v -> {
                    sb.append(String.format(formatStr,
                              v.get("name").toString(),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("cur").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1d").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1w").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1m").toString())));
                });
    }
    private void processWorkerPools(StringBuilder sb, Object obj) {
        String formatStr = "%10s: %3d  %3d  %3d  %3d\n";
        List<Map> list = (List<Map>)obj;
        list.stream()
                .forEach(v -> {
                    sb.append(String.format(formatStr,
                              v.get("name").toString(),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("cur").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1d").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1w").toString()),
                              (int)Double.parseDouble(((Map)v.get("hashrate")).get("1m").toString())));
                });
    }
    private void processGPUInfo(StringBuilder sb, List<Map> gpus, String field) {
        String formatStr = "  GPU %02d:  %3.0f  %3.0f  %3.0f  %3.0f\n";
        gpus.stream().forEach(r -> {
            sb.append(r.get("rig").toString()).append(".");
            ((List<Map>)r.get("workers")).stream().forEach(w -> {
                sb.append(w.get("name").toString()).append("\n");
                sb.append(String.format("%9s  %3s  %3s  %3s  %3s\n", "", "cur", "1d", "1w", "1m"));
                ((List<Map>)w.get("gpus")).stream().forEach(g -> {
                    Map map = (Map)g.get(field);
                    sb.append(String.format(formatStr,
                               g.get("id"),
                               map.get("cur"),
                               map.get("1d"),
                               map.get("1w"),
                               map.get("1m")));
                });
            });
        });
    }
    private List<Map> getJsonData(String pathParam) {
        URL url;
        String URLStr = infoServiceURLStr + "/" + pathParam;
        try {
            url = new URL(URLStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed url exception: " + URLStr);
        }
        List<Map> res = restTemplate.build().getForObject(URLStr, List.class);
        return res;
    }

}
