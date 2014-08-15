package pt.ipb.dthor;

import java.io.IOException;
import net.tomp2p.peers.Number160;
import pt.ipb.dthor.torrent.DThorTorrent;

public interface IDThor {
    public void addTorrent(DThorTorrent torrent) throws IOException;
    
    public DThorTorrent searchTorrent(Number160 key) throws ClassNotFoundException, IOException;
}
