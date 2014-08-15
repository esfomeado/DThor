package pt.ipb.dthor.torrent;

import com.turn.ttorrent.bcodec.BDecoder;
import com.turn.ttorrent.bcodec.BEValue;
import com.turn.ttorrent.bcodec.BEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;

public class TorrentParser {

    public static DThorTorrent parseTorrent(byte[] data) throws IOException, URISyntaxException {
        DThorTorrent torrent = new DThorTorrent();

        Map<String, BEValue> decoded = BDecoder.bdecode(new ByteArrayInputStream(data)).getMap();
        Map<String, BEValue> decoded_info = decoded.get("info").getMap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BEncoder.bencode(decoded_info, baos);

        byte[] encoded_info = baos.toByteArray();

        torrent.setInfoHash(DigestUtils.sha1(encoded_info));
        torrent.setAnnounce(decoded.get("announce").getString());

        if(decoded.containsKey("announce-list")) {
            List<List<URI>> announceList = new ArrayList<>();
            List<BEValue> tiers = decoded.get("announce-list").getList();
            for(BEValue t : tiers) {
                List<BEValue> trackers = t.getList();

                List<URI> tier = new ArrayList<>();
                for(BEValue tracker : trackers) {
                    URI uri = new URI(tracker.getString());
                    if(!tier.contains(uri)) {
                        tier.add(uri);
                    }
                }

                if(!tier.isEmpty()) {
                    announceList.add(tier);
                }
            }
            torrent.setAnnounceList(announceList);
        }

        if(decoded.containsKey("encoding")) {
            torrent.setEncoding(decoded.get("encoding").getString());
        } else {
            torrent.setEncoding(null);
        }

        if(decoded.containsKey("creation date")) {
            torrent.setCreationDate(decoded.get("creation date").getLong());
        } else {
            torrent.setCreationDate(null);
        }

        if(decoded.containsKey("comment")) {
            torrent.setComment(decoded.get("comment").getString());
        } else {
            torrent.setComment(null);
        }

        if(decoded.containsKey("created by")) {
            torrent.setCreatedBy(decoded.get("created by").getString());
        } else {
            torrent.setCreatedBy(null);
        }

        if(decoded_info.containsKey("piece length")) {
            torrent.setPieceLength(decoded_info.get("piece length").getInt());
        }

        if(decoded_info.containsKey("pieces")) {
            torrent.setPieceHash(decoded_info.get("pieces").getBytes());
        }

        torrent.setSaveAs(decoded_info.get("name").getString());

        List<String> files = new ArrayList<>();
        List<Long> filesLength = new ArrayList<>();

        if(decoded_info.containsKey("files")) {
            long totalLength = 0;
            for(BEValue file : decoded_info.get("files").getList()) {
                Map<String, BEValue> fileInfo = file.getMap();
                StringBuilder path = new StringBuilder();
                for(BEValue pathElement : fileInfo.get("path").getList()) {
                    path.append(File.separator).append(pathElement.getString());
                }
                totalLength += fileInfo.get("length").getLong();
                files.add(path.toString());
                filesLength.add(fileInfo.get("length").getLong());
            }
            torrent.setTotalLength(totalLength);
        } else {
            torrent.setTotalLength(decoded_info.get("length").getLong());
            files.add(decoded_info.get("name").getString());
            filesLength.add(decoded_info.get("length").getLong());
        }

        torrent.setFiles(files);

        torrent.setFilesLength(filesLength);

        return torrent;
    }

    public static byte[] makeTorrent(DThorTorrent t) throws UnsupportedEncodingException, IOException {
        Map<String, BEValue> torrent = new HashMap<>();

        torrent.put("announce", new BEValue(t.getAnnounce()));

        if(!t.getAnnounceList().isEmpty()) {
            List<BEValue> tiers = new LinkedList<>();
            for(List<URI> trackers : t.getAnnounceList()) {
                List<BEValue> tierInfo = new LinkedList<>();
                for(URI trackerURI : trackers) {
                    tierInfo.add(new BEValue(trackerURI.toString()));
                }
                tiers.add(new BEValue(tierInfo));
            }
            torrent.put("announce-list", new BEValue(tiers));
        }

        if(t.getEncoding() != null) {
            torrent.put("encoding", new BEValue(t.getEncoding()));
        }

        if(t.getCreationDate() != null) {
            torrent.put("creation date", new BEValue(t.getCreationDate()));
        }

        if(t.getComment() != null) {
            torrent.put("comment", new BEValue(t.getComment()));
        }

        if(t.getCreatedBy() != null) {
            torrent.put("created by", new BEValue(t.getCreatedBy()));
        }

        Map<String, BEValue> info = new TreeMap<>();
        info.put("name", new BEValue(t.getSaveAs()));

        List<BEValue> fileInfo = new LinkedList<>();
        if(t.getFiles().size() > 1) {
            for(int i = 0; i < t.getFiles().size(); i++) {
                Map<String, BEValue> fileMap = new HashMap<>();
                fileMap.put("length", new BEValue(t.getFilesLength().get(i)));

                LinkedList<BEValue> filePath = new LinkedList<>();
                File file = new File(t.getFiles().get(i));

                while(file.getParent() != null) {
                    filePath.addFirst(new BEValue(file.getName()));
                    file = file.getParentFile();
                }
                fileMap.put("path", new BEValue(filePath));
                fileInfo.add(new BEValue(fileMap));
            }
            info.put("files", new BEValue(fileInfo));

        } else {
            info.put("length", new BEValue(t.getFilesLength().get(0)));
        }

        info.put("piece length", new BEValue(t.getPieceLength()));
        info.put("pieces", new BEValue(t.getPieceHash()));
        torrent.put("info", new BEValue(info));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder.bencode(new BEValue(torrent), baos);

        return baos.toByteArray();
    }
}
