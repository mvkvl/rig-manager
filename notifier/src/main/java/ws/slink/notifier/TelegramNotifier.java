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
//    private final String urlString = "https://api.telegram.org/bot{token}/sendMessage";

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
//            conn.setConnectTimeout(15000);
//            conn.setReadTimeout(15000);
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



































    /*
    private void __sendMessage(String apiToken, String chatId, String message, int icon, String proxyUrl) {

        URL url = null;

        if (null != proxyUrl && !proxyUrl.isEmpty()) {
            try {
                url = new URL(proxyUrl);
//                System.out.println(" >>> created proxy URL");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (null != url) {
            Unirest.setProxy(new HttpHost(url.getHost(), url.getPort()));
//            System.out.println(" >>> setting proxy");
        }

        String msg = (icon > 0) ?
                     new StringBuilder().appendCodePoint(icon).toString() + message :
                     message;
        try {
            Unirest.get(urlString)
                   .header("accept", "application/json")
                   .routeParam("token", apiToken)
                   .queryString("parse_mode", "html")
                   .queryString("chat_id", chatId)
                   .queryString("text", msg)
                   .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

//    private final int MAX_ATTEMPTS = 3;
//    private final int BASE_DELAY   = 3000;

//        sendMessage(String.format(urlFormatString, apiToken, chatId, message), null);
//        sendMessage(String.format(urlFormatString, apiToken, chatId, message), null);
//        sendMessage(String.format(urlFormatString, apiToken, chatId, message), null);
//        sendMessage(String.format(urlFormatString, apiToken, chatId, message), null);

//    public void sendMessage(String apiToken, String chatId, String message, int symbol) {
//        String msg = new StringBuilder().appendCodePoint(symbol).toString() +
//                     message;
//        sendMessage(apiToken, chatId, msg);
//    }
//    public void sendMessage(String apiToken, String chatId, String message, String proxyUrlString) {
//        //        System.out.println(String.format(urlFormatString, apiToken, chatId, message));
//        if (null == proxyUrlString || proxyUrlString.isEmpty())
//            sendMessage(apiToken, chatId, message);
//        else {
//            try {
//                URL proxyURL = new URL(proxyUrlString);
//
//                Proxy.Type type = Proxy.Type.DIRECT;
//                if ("http".equalsIgnoreCase(proxyURL.getProtocol())) {
//                    type = Proxy.Type.HTTP;
//                } else if ("socks".equalsIgnoreCase(proxyURL.getProtocol())) {
//                    type = Proxy.Type.SOCKS;
//                }
//
//                sendMessage(String.format(urlFormatString, apiToken, chatId, message),
//                        new Proxy(type, new InetSocketAddress(proxyURL.getHost(), proxyURL.getPort())));
//
//            } catch (MalformedURLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//    public void sendMessage(String apiToken, String chatId, String message, int symbol, String proxyUrlString) {
//        String msg = new StringBuilder().appendCodePoint(symbol).toString() + message;
//        if (null != proxyUrlString && !proxyUrlString.isEmpty())
//            sendMessage(apiToken, chatId, msg, proxyUrlString);
//        else
//            sendMessage(apiToken, chatId, message);
//    }

/*String urlString, Proxy proxy*/

//        int cnt = 0;
//        while (cnt++ < MAX_ATTEMPTS) {
//            try {
//                URL url = new URL(urlString);
//                URLConnection conn = (null == proxy) ?
//                            url.openConnection() :
//                            url.openConnection(proxy);
//                StringBuilder sb = new StringBuilder();
//                InputStream is = new BufferedInputStream(conn.getInputStream());
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String inputLine = "";
//                while ((inputLine = br.readLine()) != null) {
//                    sb.append(inputLine);
//                }
//                conn.setConnectTimeout(15000);
//                conn.setReadTimeout(15000);
//                break;
//            } catch (MalformedURLException ex) {
//                ex.printStackTrace();
//                try {
//                    Thread.sleep(cnt * BASE_DELAY);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                try {
//                    Thread.sleep(cnt * BASE_DELAY);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
