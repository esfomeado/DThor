package pt.ipb.dthor.torrent;

import java.util.ArrayList;

public class DThorTorrent {

    private String announce;
    private String comment;
    private String createdBy;
    private long creationDate;
    private String encoding;
    private byte[] info_hash_as_binary;
    private String info_hash_as_hex;
    private String info_hash_as_url;
    private ArrayList<String> length;
    private ArrayList<String> name;
    private ArrayList<String> piece_hash_values_as_binary;
    private ArrayList<String> piece_hash_values_as_hex;
    private ArrayList<String> piece_hash_values_as_url;
    private int pieceLength;
    private String saveAs;
    private long totalLength;

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public byte[] getInfo_hash_as_binary() {
        return info_hash_as_binary;
    }

    public void setInfo_hash_as_binary(byte[] info_hash_as_binary) {
        this.info_hash_as_binary = info_hash_as_binary;
    }

    public String getInfo_hash_as_hex() {
        return info_hash_as_hex;
    }

    public void setInfo_hash_as_hex(String info_hash_as_hex) {
        this.info_hash_as_hex = info_hash_as_hex;
    }

    public String getInfo_hash_as_url() {
        return info_hash_as_url;
    }

    public void setInfo_hash_as_url(String info_hash_as_url) {
        this.info_hash_as_url = info_hash_as_url;
    }

    public ArrayList<String> getLength() {
        return length;
    }

    public void setLength(ArrayList<String> length) {
        this.length = length;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getPiece_hash_values_as_binary() {
        return piece_hash_values_as_binary;
    }

    public void setPiece_hash_values_as_binary(ArrayList<String> piece_hash_values_as_binary) {
        this.piece_hash_values_as_binary = piece_hash_values_as_binary;
    }

    public ArrayList<String> getPiece_hash_values_as_hex() {
        return piece_hash_values_as_hex;
    }

    public void setPiece_hash_values_as_hex(ArrayList<String> piece_hash_values_as_hex) {
        this.piece_hash_values_as_hex = piece_hash_values_as_hex;
    }

    public ArrayList<String> getPiece_hash_values_as_url() {
        return piece_hash_values_as_url;
    }

    public void setPiece_hash_values_as_url(ArrayList<String> piece_hash_values_as_url) {
        this.piece_hash_values_as_url = piece_hash_values_as_url;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    public void setPieceLength(int pieceLength) {
        this.pieceLength = pieceLength;
    }

    public String getSaveAs() {
        return saveAs;
    }

    public void setSaveAs(String saveAs) {
        this.saveAs = saveAs;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }
}