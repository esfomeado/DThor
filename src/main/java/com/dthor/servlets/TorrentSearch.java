package com.dthor.servlets;

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
import com.dthor.DThorConfig;
import com.dthor.DThorTomP2P;
import com.dthor.torrent.DThorTorrent;
import com.dthor.torrent.TorrentParser;

public class TorrentSearch extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");

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
        if(!array.isEmpty()) {
            for(Object array1 : array) {
                JSONObject a = (JSONObject) array1;
                table += "<tr><td>";
                table += "<a class=\"torrent_link\" href=\"?key=" + a.get("key") + "\">";
                table += "<label class=\"torrent_name\">" + a.get("title") + "</label>";
                table += "</a></td>";
                table += "<td><a class=\"torrent_link\" data-toggle=\"modal\" data-target=\"#deleteTorrent\" data-id=\"/" + a.get("key") + "\">";
                table += "<label class=\"torrent_delete\">Apagar</label></a></td></tr>";
            }

            request.setAttribute("query", query);
            request.setAttribute("table", table);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("table", table);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
            rd.forward(request, response);
        }
    }
}
