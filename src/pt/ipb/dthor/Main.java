package pt.ipb.dthor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
            
    public static JettyServer server;

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        
        server = JettyServer.getInstance();
        server.waitForInterrupt();
    }
}