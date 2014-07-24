package pt.ipb.dthor.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pt.ipb.dthor.DThorTomP2P;
import pt.ipb.dthor.torrent.DThorTorrent;

public class TorrentSearch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DThorTomP2P dht;
        try {
            dht = DThorTomP2P.getInstance();

            ArrayList<DThorTorrent> torrents = dht.searchTorrent(request.getParameter("torrentName"));

            for (DThorTorrent t : torrents) {
                response.getWriter().println(t.getSaveAs());
            }
        } catch (Exception ex) {
            Logger.getLogger(TorrentSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
