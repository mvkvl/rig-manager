package ws.slink.notifier;

public class Tester {

    public static void main(String [] args) {
        String token = "554857640:AAFfUt5ihoNfu25MpBmjZ-9SQ53C3iKRxgc";
        String chat = "332509330";
        String proxy = "http://127.0.0.1:8123";
        String message = "<b>TEST</b>: test message";
        int icon = 0x0001F4A1;
        TelegramNotifier.instance().sendMessage(token, chat, message, icon, proxy); // 0x0001F4A5
    }
}

// 0x0001F4A1 - light bulb
// 0x0001F5F8