package ws.slink.mine.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ws.slink.mine.data.BotData;
import ws.slink.telegram.menu.*;

public class RigBotMenu extends AbstractBotMenu {

    private static final Logger logger = LoggerFactory.getLogger(RigBotMenu.class);

    @Autowired
    private BotData botData;

    public RigBotMenu(String botTitle) {
        super(botTitle);
    }

    private TreeNode balanceMenu() {
        return new DialogCommand(" Balance ",
                "command_balances",
                (chat, message) -> botData.getBalanceData());
    }
    private TreeNode workerMenu() {
        return new DialogCommand(" Worker ",
                "command_workers",
                (chat, message) -> botData.getWorkerData());
    }
    private TreeNode rigMenu() {
        TreeNode menu = new DialogItem(" Rig ", "main_menu_rig");
        menu.add(new DialogCommand(" hash ",
                "sub_menu_rig_hashrate",
                (chat, message) -> botData.getGPUHashrateData()));
        menu.add(new DialogCommand(" temp ",
                "sub_menu_rig_temperature",
                (chat, message) -> botData.getGPUTemperatureData()));
        menu.add(new DialogCommand(" power ",
                "sub_menu_rig_power",
                (chat, message) -> botData.getGPUPowerData()));
        menu.add(new ClearDialogCommand("sub_menu_rig_reset"));
        menu.add(new BackDialogCommand("sub_menu_rig_back"));
        return menu;
    }

    @Override protected void build() {
        getRoot().add(rigMenu());
        getRoot().add(workerMenu());
        getRoot().add(balanceMenu());
        getRoot().add(new ClearDialogCommand("main_menu_reset"));
    }
}
