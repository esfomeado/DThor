package pt.ipb.dthor.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pt.ipb.dthor.DThorTomP2P;
import pt.ipb.dthor.torrent.DThorTorrent;
import pt.ipb.dthor.torrent.TorrentParser;

public class TorrentUpload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File writeDir = new File(tempDir.toString(), "dthor");

        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                String name = null;

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        name = new File(item.getName()).getName();
                        item.write(new File(writeDir.toString() + File.separator + name));
                    }
                }
                
                Path path = Paths.get(writeDir.toString() + File.separator + name);
                DThorTorrent torrent = TorrentParser.parseTorrent(Files.readAllBytes(path));
                
                DThorTomP2P dht = DThorTomP2P.getInstance();
                dht.addTorrent(torrent);

                request.setAttribute("message", "Upload Sucedded!");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
                rd.forward(request, response);

            } catch (Exception ex) {
                request.setAttribute("message", "Upload failed: " + ex);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            }
        }
    }
}
