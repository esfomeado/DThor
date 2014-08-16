package pt.ipb.dthor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Random;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number640;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import pt.ipb.dthor.torrent.DThorTorrent;

public class DThorTomP2P implements IDThor {

    private final boolean IS_SUPER_PEER = DThorConfig.IS_SUPER_PEER;
    private final String SUPER_PEER_IP = DThorConfig.SUPER_PEER_IP;
    private final int SUPER_PEER_PORT = DThorConfig.SUPER_PEER_PORT;
    private static DThorTomP2P instance = null;
    private final PeerDHT peer;

    public static DThorTomP2P getInstance() throws IOException, Exception {
        if(instance == null) {
            instance = new DThorTomP2P();
        }
        return instance;
    }

    private DThorTomP2P() throws IOException, Exception {
        Random r = new Random();
        if(IS_SUPER_PEER) {
            peer = new PeerBuilderDHT(new PeerBuilder(new Number160(r)).ports(SUPER_PEER_PORT).start()).start();
        } else {
            int listenPort = SUPER_PEER_PORT + 1 + (int) Math.floor(Math.random() * 1000);
            peer = new PeerBuilderDHT(new PeerBuilder(new Number160(r)).ports(listenPort).start()).start();
            PeerAddress peerAddress = new PeerAddress(Number160.ZERO, InetAddress.getByName(SUPER_PEER_IP), SUPER_PEER_PORT, SUPER_PEER_PORT);

            FutureDiscover discovery = peer.peer().discover().peerAddress(peerAddress).start();
            discovery.awaitUninterruptibly();

            if(discovery.isFailed()) {
                //Executar NAT
            }

            peerAddress = discovery.peerAddress();

            FutureBootstrap bootstrap = peer.peer().bootstrap().peerAddress(peerAddress).start();
            bootstrap.awaitUninterruptibly();

            if(bootstrap.isFailed()) {
                throw new Exception("Failed to bootstrap to host " + bootstrap.failedReason());
            }
        }

        System.out.println("Peer Connected!");
    }

    @Override
    public void addTorrent(DThorTorrent torrent) throws IOException {
        Data torrentData = new Data(torrent);
        Number160 key = torrentData.hash();

        FuturePut fp = peer.put(key).object(torrent).start();
        fp.awaitUninterruptibly();
    }

    @Override
    public DThorTorrent searchTorrent(Number160 key) throws ClassNotFoundException, IOException {
        FutureGet fg = peer.get(key).start();
        fg.awaitUninterruptibly();

        Data torrentData = fg.data();

        return (DThorTorrent) torrentData.object();
    }

    public ArrayList<Number160> getKeys() {
        ArrayList<Number160> keys = new ArrayList<>();

        NavigableMap<Number640, Data> keysMap = peer.storageLayer().get();

        for(Number640 key : keysMap.keySet()) {
            keys.add(key.locationKey());
        }

        return keys;
    }

    public void sendKeys(final ArrayList<Number160> keys) {
        peer.peer().objectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) throws Exception {
                return keys;
            }
        });
    }
}
