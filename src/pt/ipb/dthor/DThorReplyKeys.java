package pt.ipb.dthor;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.peers.Number160;

public class DThorReplyKeys implements Runnable {

    @Override
    public void run() {
        try {
            DThorTomP2P dht = DThorTomP2P.getInstance();
            while (true) {
                ArrayList<Number160> keys = dht.getKeys();
                dht.sendKeys(keys);
            }
        } catch (Exception ex) {
            Logger.getLogger(DThorReplyKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
