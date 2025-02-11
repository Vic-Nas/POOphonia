package models;

import java.util.Arrays;

public class Album extends MusicItem {

    private int numberOfTracks;
    private String artist, label;

    public Album(String[] parts) {
        this.setElements(Arrays.copyOfRange(parts, 0, 3));
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
    public String to_csv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return String.format("Album %s with %d by %s",
                this.getTitle(), this.getNumberOfTracks(), this.getArtist());
    }
}
