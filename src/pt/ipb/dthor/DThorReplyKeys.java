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
            int i = 0;
            while (true) {
                ArrayList<Number160> keys = dht.getKeys();
                dht.sendKeys(keys);
                Thread.sleep(5000);
            }
        } catch (Exception ex) {
            Logger.getLogger(DThorReplyKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
