package models;

import java.util.Arrays;

public class Album extends MusicItem {

    private int numberOfTracks;
    private String artist, label;

    public Album(String[] parts) {
        this.setCommons(Arrays.copyOfRange(parts, 0, 3));
        this.artist = parts[3];
        this.label = parts[4];
        this.numberOfTracks = Integer.parseInt(parts[5]);
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toCSV() {
        return String.format("album,%d,%s,%d,%s,%s,%d", this.getId(), this.getTitle(),
                this.getReleaseYear(), this.getArtist(), this.getLabel(), this.getNumberOfTracks());
    }

    @Override
    public String info() {
        return String.format("Album %s of %d with %d tracks by %s",
                this.getTitle(), this.getReleaseYear(), this.getNumberOfTracks(), this.getArtist());
    }

    @Override
    public String toString() {
        return String.format("Album [ID=%d, Title=%s, Release Year=%d, Artist=%s, Tracks=%d, Label=%s]",
                this.getId(), this.getTitle(), this.getReleaseYear(), this.getArtist(), this.getNumberOfTracks(), this.getLabel());

    }
}
