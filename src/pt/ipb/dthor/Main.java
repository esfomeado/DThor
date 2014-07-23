package pt.ipb.dthor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
            
    private static JettyServer server;
	private static DThorTomP2P dht;

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        
		dht = DThorTomP2P.getInstance();
		
        server = JettyServer.getInstance();
        server.waitForInterrupt();
    }
}