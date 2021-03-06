package ws.slink.mine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ws.slink.mine.bot.RigBot;
import ws.slink.mine.bot.RigBotMenu;
import ws.slink.telegram.BotBuilder;
import ws.slink.telegram.menu.BotMenu;

@SpringBootApplication(scanBasePackages={"ws.slink"})
public class TeleBotApplication {

    @Autowired private BotBuilder<RigBot> botBuilder;

    @Bean public RigBot getBot() {
        return botBuilder.build(RigBot.class.getName());
    }

    @Bean public BotMenu getBotMenu() {
        return new RigBotMenu("mine");
    }

    @Bean public CommandLineRunner applicationRunner() {
        return new TeleBotRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(TeleBotApplication.class, args);
    }

}
