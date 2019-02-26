package ws.slink.notifier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotifierBean {

    @Value("${notification.telegram.proxy:}")
    private String proxyUrl;
    @Value("${notification.telegram.token:}")
    private String apiToken;
    @Value("${notification.telegram.chat:}")
    private String chatId;
    @Value("${notification.telegram.icon:0x0001F4A1}")
    private int icon;

    public void sendMessage(String message, int messageIcon) {
        if (StringUtils.isNotBlank(apiToken) && StringUtils.isNotBlank(chatId))
            TelegramNotifier.instance().sendMessage(apiToken,
                    chatId,
                    message,
                    messageIcon,
                    proxyUrl);
    }
    public void sendMessage(String message) {
        if (StringUtils.isNotBlank(apiToken) && StringUtils.isNotBlank(chatId))
            TelegramNotifier.instance().sendMessage(apiToken,
                    chatId,
                    message,
                    icon,
                    proxyUrl);
    }


}
