package ws.slink.notifier;

// urlFormatString = "https://api.telegram.org/bot%s/sendMessage?parse_mode=html&chat_id=%s&text=%s";

import java.io.*;
import java.net.*;

public class TelegramNotifier {

    private static TelegramNotifier ourInstance = new TelegramNotifier();
    public static TelegramNotifier instance() {
        return ourInstance;
    }

    private TelegramNotifier() {}

    private final String urlFormatString = "https://api.telegram.org/bot%s/sendMessage?parse_mode=html&chat_id=%s&text=%s";

    public void sendMessage(String apiToken, String chatId, String message) {
        __sendMessage(apiToken, chatId, message, -1, null);
    }
    public void sendMessage(String apiToken, String chatId, String message, String proxyStr) {
        __sendMessage(apiToken, chatId, message, -1, proxyStr);
    }
    public void sendMessage(String apiToken, String chatId, String message, int icon) {
        __sendMessage(apiToken, chatId, message, -1, null);
    }
    public void sendMessage(String apiToken, String chatId, String message, int icon, String proxyStr) {
        __sendMessage(apiToken, chatId, message, icon, proxyStr);
    }

    private void __sendMessage(String apiToken, String chatId, String message, int icon, String proxyUrl) {
        String msg = (icon > 0) ?
                new StringBuilder().appendCodePoint(icon).toString() + message :
                message;
        try {
            URL url = new URL(String.format(urlFormatString, apiToken, chatId, msg));
            URLConnection conn = org.apache.commons.lang3.StringUtils.isBlank(proxyUrl) ?
                    url.openConnection() :
                    url.openConnection(getProxy(proxyUrl));
            try ( InputStream    is = new BufferedInputStream(conn.getInputStream());
                  BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    sb.append(inputLine);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Proxy getProxy(String proxyUrlStr) {
        try {
            URL proxyUrl = new URL(proxyUrlStr);
            Proxy.Type type = Proxy.Type.DIRECT;
            if ("http".equalsIgnoreCase(proxyUrl.getProtocol())) {
                type = Proxy.Type.HTTP;
            } else if ("socks".equalsIgnoreCase(proxyUrl.getProtocol())) {
                type = Proxy.Type.SOCKS;
            }
            return new Proxy(type, new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
