package pt.ipb.dthor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        
        JettyServer server = new JettyServer();
        server.start();
        server.waitForInterrupt();
    }
}
