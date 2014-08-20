package pt.ipb.dthor.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.tomp2p.peers.Number160;
import pt.ipb.dthor.DThorConfig;
import pt.ipb.dthor.DThorTomP2P;

public class TorrentDelete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String deleteKey = request.getParameter("deleteKey");
        String key = request.getParameter("key");

        try {
            DThorTomP2P dht = DThorTomP2P.getInstance();

            if(dht.deleteTorrent(new Number160(key), deleteKey)) {
                URL searchEngineURL = new URL("http://" + DThorConfig.SEARCH_IP + ":" + DThorConfig.SEARCH_PORT + "/delete?key=" + key);
                HttpURLConnection connection = (HttpURLConnection) searchEngineURL.openConnection();
                connection.getResponseCode();

                response.setContentType("text/plain");
                response.getWriter().write("Sucess!");

            } else {
                response.setContentType("text/plain");
                response.getWriter().write("Invalid Key!");
            }
        } catch (Exception ex) {
            Logger.getLogger(TorrentDelete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
