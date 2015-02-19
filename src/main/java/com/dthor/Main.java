package com.dthor;

import java.awt.*;
import java.net.URI;

public class Main {

    public static void main(String[] args) throws Exception {
        DThorConfig config = new DThorConfig();
        config.load();

        DThorTomP2P.getInstance();

        DThorReplyKeys reply = new DThorReplyKeys();
        Thread replyThread = new Thread(reply);
        replyThread.start();

        Desktop.getDesktop().browse(new URI("http://localhost:"+DThorConfig.JETTY_PORT));

        JettyServer server = JettyServer.getInstance();
        server.waitForInterrupt();
    }

}
