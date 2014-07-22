package pt.ipb.dthor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.nat.FutureNAT;
import net.tomp2p.nat.FutureRelayNAT;
import net.tomp2p.nat.PeerBuilderNAT;
import net.tomp2p.nat.PeerNAT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import pt.ipb.dthor.torrent.DThorTorrent;

public class DThorTomP2P implements IDThor {

    private static DThorTomP2P instance = null;
    private final String MASTER_IP = "193.137.109.120";
    private final int MASTER_PORT = 4000;
    private Peer peer = null;

    public static DThorTomP2P getInstance() throws IOException, Exception {
        if (instance == null) {
            instance = new DThorTomP2P();
        }
        return instance;
    }

    private DThorTomP2P() throws IOException, Exception {
        Random r = new Random(43L);
        int listenPort = MASTER_PORT + 1 + (int) Math.floor(Math.random() * 1000);
        peer = new PeerBuilder(new Number160(r)).ports(listenPort).behindFirewall().start();
        PeerNAT peerNAT = new PeerBuilderNAT(peer).start();
        PeerAddress peerAddress = new PeerAddress(Number160.ZERO, InetAddress.getByName(MASTER_IP), MASTER_PORT, MASTER_PORT);

        FutureDiscover fd = peer.discover().peerAddress(peerAddress).start();

        if (fd.isSuccess()) {
            System.out.println("Outside Address: " + fd.peerAddress());
        } else {
            System.out.println("Failed discover: " + fd.failedReason());
        }

        FutureNAT fn = peerNAT.startSetupPortforwarding(fd);
        FutureRelayNAT frn = peerNAT.startRelay(fd, fn);
        frn.awaitUninterruptibly();

        peerAddress = fd.reporter();
        FutureBootstrap bootstrap = peerNAT.peer().bootstrap().peerAddress(peerAddress).start();
        bootstrap.awaitUninterruptibly();

        if (bootstrap.isFailed()) {
            throw new Exception("Failed to bootstrap to host " + bootstrap.failedReason());
        }        
    }

    @Override
    public void addTorrent(DThorTorrent torrent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<DThorTorrent> searchTorrent(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
