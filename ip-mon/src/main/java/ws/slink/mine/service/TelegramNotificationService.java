package ws.slink.mine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws.slink.notifier.TelegramNotifierBean;

@Service
public class TelegramNotificationService implements NotificationService {

    @Autowired
    private TelegramNotifierBean telegramNotifier;

    @Override
    public void notify(String message) {
        telegramNotifier.sendMessage(message);
    }

    @Override
    public void notify(String message, int icon) {
        telegramNotifier.sendMessage(message, icon);
    }
}
