package pt.ipb.dthor.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.tomp2p.peers.Number160;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import pt.ipb.dthor.DThorConfig;
import pt.ipb.dthor.DThorTomP2P;
import pt.ipb.dthor.torrent.DThorTorrent;
import pt.ipb.dthor.torrent.TorrentParser;

public class TorrentSearch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");

        if(key != null) {

            try {
                DThorTomP2P dht = DThorTomP2P.getInstance();
                DThorTorrent torrent = dht.searchTorrent(new Number160(key));
                String torrentName = torrent.getSaveAs() + ".torrent";

                response.setContentType("text/plain");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + torrentName + "\"");

                byte[] torrentData = TorrentParser.makeTorrent(torrent);

                OutputStream os = response.getOutputStream();
                os.write(torrentData);
                os.flush();
                os.close();

            } catch (Exception ex) {
                Logger.getLogger(TorrentSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        URL searchEngineURL = new URL("http://" + DThorConfig.SEARCH_IP + ":" + DThorConfig.SEARCH_PORT + "/remotesearch?query=" + query);

        HttpURLConnection connection = (HttpURLConnection) searchEngineURL.openConnection();
        connection.setDoOutput(true);

        BufferedReader result = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String decodedResult;
        String x = null;

        while((decodedResult = result.readLine()) != null) {
            x = decodedResult;
        }

        Object obj = JSONValue.parse(x);
        JSONArray array = (JSONArray) obj;

        result.close();
        connection.disconnect();

        String table = "";
        for(Object array1 : array) {
            JSONObject a = (JSONObject) array1;
            table += "<tr><td>";
            table += "<a class=\"torrent_link\" href=\"?key=" + a.get("key") + "\">";
            table += "<label class=\"torrent_name\">" + a.get("title") + "</label>";
            table += "</a></td></tr>";
        }

        request.setAttribute("query", query);
        request.setAttribute("table", table);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");
        rd.forward(request, response);

    }
}
