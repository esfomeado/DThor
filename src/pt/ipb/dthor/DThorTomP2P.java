package pt.ipb.dthor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Random;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.FutureRemove;
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
import net.tomp2p.peers.Number640;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.relay.FutureRelay;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import org.apache.commons.lang3.RandomStringUtils;
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
            System.out.println("Super Peer started!");
        } else {
            int listenPort = SUPER_PEER_PORT + 1 + (int) Math.floor(Math.random() * 1000);
            peer = new PeerBuilderDHT(new PeerBuilder(new Number160(r)).ports(listenPort).start()).start();
            PeerAddress peerAddress = new PeerAddress(Number160.ZERO, InetAddress.getByName(SUPER_PEER_IP), SUPER_PEER_PORT, SUPER_PEER_PORT);

            FutureDiscover discovery = peer.peer().discover().peerAddress(peerAddress).start();
            discovery.awaitUninterruptibly();

            if(discovery.isSuccess()) {
                peerAddress = discovery.peerAddress();

                FutureBootstrap bootstrap = peer.peer().bootstrap().peerAddress(peerAddress).start();
                bootstrap.awaitUninterruptibly();

                if(bootstrap.isSuccess()) {
                    System.out.println("Peer Connected!");
                } else {
                    throw new Exception("Failed to bootstrap to host " + bootstrap.failedReason());

                }
            } else {
                PeerNAT peerNAT = new PeerBuilderNAT(peer.peer()).start();
                FutureNAT futureNAT = peerNAT.startSetupPortforwarding(discovery);
                FutureRelayNAT frn = peerNAT.startRelay(discovery, futureNAT);

                if(frn.isFailed()) {
                    PeerAddress serverPeerAddress = peer.peerBean().serverPeerAddress();
                    serverPeerAddress = serverPeerAddress.changeFirewalledTCP(true).changeFirewalledUDP(true);
                    peer.peerBean().serverPeerAddress(serverPeerAddress);

                    FutureBootstrap fb = peer.peer().bootstrap().peerAddress(peerAddress).start();
                    fb.awaitUninterruptibly();

                    if(fb.isSuccess()) {
                        FutureRelay futureRelay = new FutureRelay();
                        futureRelay.awaitUninterruptibly();

                        if(futureRelay.isSuccess()) {
                            FutureBootstrap fb2 = peer.peer().bootstrap().peerAddress(peerAddress).start();
                            fb2.awaitUninterruptibly();

                            System.out.println("PeerNAT Connected!");
                        }
                    }
                }
            }
        }
    }

    @Override
    public String addTorrent(DThorTorrent torrent) throws IOException {
        Data torrentData = new Data(torrent);
        Number160 key = torrentData.hash();

        String deleteKey = RandomStringUtils.randomAlphanumeric(16);
        torrent.setDeleteKey(deleteKey);

        FuturePut fp = peer.put(key).object(torrent).start();
        fp.awaitUninterruptibly();

        return deleteKey;
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

    @Override
    public boolean deleteTorrent(Number160 key, String deleteKey) throws ClassNotFoundException, IOException {
        boolean result = false;
        DThorTorrent torrent = searchTorrent(key);

        if(torrent.getDeleteKey().equalsIgnoreCase(deleteKey)) {
            FutureRemove fr = peer.remove(key).start();
            fr.awaitUninterruptibly();
            result = true;
        }

        return result;
    }

}
