package pt.ipb.dthor;

import java.util.ArrayList;
import pt.ipb.dthor.torrent.DThorTorrent;

public interface IDThor {

    public void addTorrent(DThorTorrent torrent);

    public ArrayList<DThorTorrent> searchTorrent(String name);
}
