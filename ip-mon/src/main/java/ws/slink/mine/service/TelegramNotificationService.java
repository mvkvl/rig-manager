package ws.slink.mine.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ws.slink.notifier.TelegramNotifier;

@Service
public class TelegramNotificationService implements NotificationService {

    @Value("${notification.telegram.token:}")
    private String apiToken;
    @Value("${notification.telegram.chat:}")
    private String chatId;
    @Value("${notification.telegram.proxy:}")
    private String proxyUrl;
    @Value("${notification.telegram.icon:0x0001F4A1}")
    private int iconConfigured;

    @Override
    public void notify(String message) {
        if (StringUtils.isNotBlank(apiToken) && StringUtils.isNotBlank(chatId))
            TelegramNotifier.instance()
                            .sendMessage(apiToken,
                                         chatId,
                                    " " + message,
                                         iconConfigured,
                                         proxyUrl);
    }

    @Override
    public void notify(String message, int icon) {
        if (StringUtils.isNotBlank(apiToken) && StringUtils.isNotBlank(chatId))
            TelegramNotifier.instance()
                            .sendMessage(apiToken,
                                         chatId,
                                         " " + message,
                                         icon,
                                         proxyUrl);
    }
}
