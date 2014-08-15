package pt.ipb.dthor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class DThorConfig {

    public static boolean SUPER_PEER;
    public static String SUPER_PEER_IP;
    public static int SUPER_PEER_PORT;
    public static int JETTY_PORT;
    public static String SEARCH_IP;
    public static String SEARCH_PORT;
    
    public void load() throws FileNotFoundException, IOException, URISyntaxException {
        URL configURL = getClass().getResource("./config.properties");

        Properties config = new Properties();
        InputStream input = new FileInputStream(configURL.toURI().getPath());
        config.load(input);

        SUPER_PEER = Boolean.valueOf(config.getProperty("superPeer"));
        SUPER_PEER_IP = config.getProperty("superPeerIP");
        SUPER_PEER_PORT = Integer.parseInt(config.getProperty("superPeerPort"));
        JETTY_PORT = Integer.parseInt(config.getProperty("jettyPort"));
        SEARCH_IP = config.getProperty("searchIP");
        SEARCH_PORT = config.getProperty("searchPort");
    }
}
