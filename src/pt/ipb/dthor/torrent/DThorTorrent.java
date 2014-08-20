package pt.ipb.dthor.torrent;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class DThorTorrent implements Serializable {

    private String announce;
    private List<List<URI>> announceList;
    private String comment;
    private String createdBy;
    private Long creationDate;
    private String encoding;
    private String saveAs;
    private int pieceLength;
    private List<String> files;
    private List<Long> filesLength;
    private byte[] infoHash;
    private Long totalLength;
    private byte[] pieceHash;
    private String deleteKey;

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public List<List<URI>> getAnnounceList() {
        return announceList;
    }

    public void setAnnounceList(List<List<URI>> announceList) {
        this.announceList = announceList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getSaveAs() {
        return saveAs;
    }

    public void setSaveAs(String saveAs) {
        this.saveAs = saveAs;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    public void setPieceLength(int pieceLength) {
        this.pieceLength = pieceLength;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<Long> getFilesLength() {
        return filesLength;
    }

    public void setFilesLength(List<Long> filesLength) {
        this.filesLength = filesLength;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(byte[] infoHash) {
        this.infoHash = infoHash;
    }

    public Long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Long totalLength) {
        this.totalLength = totalLength;
    }

    public byte[] getPieceHash() {
        return pieceHash;
    }

    public void setPieceHash(byte[] pieceHash) {
        this.pieceHash = pieceHash;
    }

    public String getDeleteKey() {
        return deleteKey;
    }

    public void setDeleteKey(String deleteKey) {
        this.deleteKey = deleteKey;
    }
}
