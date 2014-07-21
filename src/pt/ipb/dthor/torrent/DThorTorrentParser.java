package pt.ipb.dthor.torrent;

import jBittorrentAPI.TorrentFile;
import jBittorrentAPI.TorrentProcessor;
import java.io.File;
import java.util.Map;

public class DThorTorrentParser {
    
    private final File torrent;
    
    public DThorTorrentParser(File torrent) {
        this.torrent = torrent;
    }
    
    public DThorTorrent parseTorrent() {
        
        TorrentProcessor parse = new TorrentProcessor();
        Map torrentMap = parse.parseTorrent(torrent);
        TorrentFile torrentFile = parse.getTorrentFile(torrentMap);
        
        DThorTorrent thorTorrent = new DThorTorrent();
        thorTorrent.setAnnounce(torrentFile.announceURL);
        thorTorrent.setComment(torrentFile.comment);
        thorTorrent.setCreatedBy(torrentFile.createdBy);
        thorTorrent.setCreationDate(torrentFile.creationDate);
        thorTorrent.setEncoding(torrentFile.encoding);
        thorTorrent.setInfo_hash_as_binary(torrentFile.info_hash_as_binary);
        thorTorrent.setInfo_hash_as_hex(torrentFile.info_hash_as_hex);
        thorTorrent.setInfo_hash_as_url(torrentFile.info_hash_as_url);
        thorTorrent.setLength(torrentFile.length);
        thorTorrent.setName(torrentFile.name);
        thorTorrent.setPieceLength(torrentFile.pieceLength);
        thorTorrent.setPiece_hash_values_as_binary(torrentFile.piece_hash_values_as_binary);
        thorTorrent.setPiece_hash_values_as_hex(torrentFile.piece_hash_values_as_hex);
        thorTorrent.setPiece_hash_values_as_url(torrentFile.piece_hash_values_as_url);
        thorTorrent.setSaveAs(torrentFile.saveAs);
        thorTorrent.setTotalLength(torrentFile.total_length);
        
        return thorTorrent;
    }
}
