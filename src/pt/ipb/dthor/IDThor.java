package pt.ipb.dthor;

import java.io.IOException;
import java.util.ArrayList;
import pt.ipb.dthor.torrent.DThorTorrent;

public interface IDThor {

    public void addTorrent(DThorTorrent torrent) throws IOException;

    public ArrayList<DThorTorrent> searchTorrent(String name) throws ClassNotFoundException, IOException;
}
