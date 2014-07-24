package pt.ipb.dthor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.nat.FutureNAT;
import net.tomp2p.nat.FutureRelayNAT;
import net.tomp2p.nat.PeerBuilderNAT;
import net.tomp2p.nat.PeerNAT;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import pt.ipb.dthor.torrent.DThorTorrent;

public class DThorTomP2P implements IDThor {

    private static DThorTomP2P instance = null;
    private final String MASTER_IP = "127.0.0.1";
    private final int MASTER_PORT = 4000;
    private PeerDHT peer = null;

    public static DThorTomP2P getInstance() throws IOException, Exception {
        if (instance == null) {
            instance = new DThorTomP2P();
        }
        return instance;
    }

    private DThorTomP2P() throws IOException, Exception {
        Random r = new Random(43L);
        int listenPort = MASTER_PORT + 1 + (int) Math.floor(Math.random() * 1000);
        peer = new PeerBuilderDHT(new PeerBuilder(new Number160(r)).ports(listenPort).behindFirewall().start()).start();
        PeerNAT peerNAT = new PeerBuilderNAT(peer.peer()).start();
        PeerAddress peerAddress = new PeerAddress(Number160.ZERO, InetAddress.getByName(MASTER_IP), MASTER_PORT, MASTER_PORT);

        FutureDiscover fd = peer.peer().discover().peerAddress(peerAddress).start();
        fd.awaitUninterruptibly();

        if (fd.isSuccess()) {
            System.out.println("Outside Address: " + fd.peerAddress());
        } else {
            System.out.println("Failed discover: " + fd.failedReason());
        }

        FutureNAT fn = peerNAT.startSetupPortforwarding(fd);
        FutureRelayNAT frn = peerNAT.startRelay(fd, fn);
        frn.awaitUninterruptibly();

        peerAddress = fd.reporter();
        FutureBootstrap bootstrap = peer.peer().bootstrap().peerAddress(peerAddress).start();
        bootstrap.awaitUninterruptibly();

        if (bootstrap.isFailed()) {
            throw new Exception("Failed to bootstrap to host " + bootstrap.failedReason());
        }
    }

    @Override
    public void addTorrent(DThorTorrent torrent) throws IOException {
        Data d = new Data(torrent);
        Number160 key = Number160.createHash(torrent.getSaveAs());

        FuturePut fp = peer.put(key).object(torrent).start();
        fp.awaitUninterruptibly();

        System.out.println(torrent.getSaveAs());
        String[] keywords = torrent.getSaveAs().split(" ");

        for (String keyword : keywords) {
            System.out.println("Keyword: " + keyword);
            Number160 keyKeyWord = Number160.createHash(keyword);
            peer.add(keyKeyWord).object(key).start().awaitUninterruptibly();
        }
    }

    @Override
    public ArrayList<DThorTorrent> searchTorrent(String name) throws ClassNotFoundException, IOException {
        Number160 keyKeyWord = Number160.createHash(name);
        FutureGet fg = peer.get(keyKeyWord).all().start();
        fg.awaitUninterruptibly();
        System.out.println("Size Chaves: " + fg.dataMap().size());
        Iterator<Data> objectos = fg.dataMap().values().iterator();

        Number160 termKey;
        ArrayList<DThorTorrent> torrents = new ArrayList<>();

        while (objectos.hasNext()) {
            termKey = (Number160) objectos.next().object();
            fg = peer.get(termKey).start();
            fg.awaitUninterruptibly();
            
            torrents.add((DThorTorrent)fg.data().object());
        }

        return torrents;
    }
}
