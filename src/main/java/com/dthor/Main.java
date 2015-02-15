package com.dthor;

import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException, Exception {
        DThorConfig config = new DThorConfig();
        config.load();

        DThorTomP2P dht = DThorTomP2P.getInstance();

        DThorReplyKeys reply = new DThorReplyKeys();
        Thread replyThread = new Thread(reply);
        replyThread.start();

        JettyServer server = JettyServer.getInstance();
        server.waitForInterrupt();
    }

}
