package ws.slink.mine.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ws.slink.telegram.menu.BotMenu;

import javax.annotation.PostConstruct;

public class RigBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(RigBot.class);

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String userName;
    @Value("${bot.owner_id:-1}")
    private int owner_id;
    @Value("${proxy:}")
    private String proxyString;

    @Autowired
    BotMenu botMenu;

    public RigBot(DefaultBotOptions opts) {
        super(opts);
    }

    @Override
    public String getBotToken() {
        return token;
    }
    @Override
    public String getBotUsername() {
        return userName;
    }
    public int getOwnerId() {
        return owner_id;
    }
    @Override
    public void onUpdateReceived(Update update) {
        long chat_id = 0;
        BotApiMethod baMessage = null;
        SendMessage response = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            chat_id = update.getMessage().getChatId();
            if (chat_id == this.owner_id) {
                if (message_text.equals("/start")) {
                    SendMessage mes = (SendMessage) botMenu.getRoot().getAction(chat_id, 0, null);
                    mes.enableHtml(true);
                    baMessage = mes;
                }
            }
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(chatId);
            String text = message.getText();
            response.setText(text);
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            chat_id          = update.getCallbackQuery().getMessage().getChatId();
            long message_id  = update.getCallbackQuery().getMessage().getMessageId();
            EditMessageText emt = (EditMessageText)botMenu.getNode(call_data).getAction(chat_id, message_id, null);
            emt.enableHtml(true);
            baMessage = emt;
        }

        if (null != baMessage)
            try {
                execute(baMessage); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        if (null != response)
            try {
                execute(baMessage); // Sending our message object to user
                logger.info("Sent message \"{}\" to {}", response.getText(), response.getChatId());
            } catch (TelegramApiException e) {
                logger.error("Failed to send message \"{}\" to {} due to error: {}", response.getText(), response.getChatId(), e.getMessage());
                e.printStackTrace();
            }

    }

    @PostConstruct
    public void start() {
        if (null == proxyString || proxyString.isEmpty())
            logger.info("Starting telegram bot\nusername: {}, token: {}", userName, token);
        else
            logger.info("Starting proxy-connected telegram bot\nusername: {}, token: {}", userName, token);
    }

}
