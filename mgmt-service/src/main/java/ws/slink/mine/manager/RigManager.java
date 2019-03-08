package ws.slink.mine.manager;

import ws.slink.mine.model.RigMessage;

public interface RigManager {

    RigMessage stop();
    RigMessage stop(String miner);

    RigMessage start();
    RigMessage start(String miner);

//    String powerUp();
//    String powerDown();

}
