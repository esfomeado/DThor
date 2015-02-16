package com.dthor;

public class Main {

    public static void main(String[] args) throws Exception {
        DThorConfig config = new DThorConfig();
        config.load();

        DThorTomP2P.getInstance();

        DThorReplyKeys reply = new DThorReplyKeys();
        Thread replyThread = new Thread(reply);
        replyThread.start();

        JettyServer server = JettyServer.getInstance();
        server.waitForInterrupt();
    }

}
