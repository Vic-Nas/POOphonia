package models;

import java.util.Arrays;

public class Song extends MusicItem {

    private int duration;
    private String artist, genre;

    public Song(String[] parts) {
        this.setElements(Arrays.copyOfRange(parts, 0, 3));
        this.artist = parts[3];
        this.duration = Integer.parseInt(parts[5]);
        this.genre = parts[4];
    }

    @Override
    public String to_csv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return String.format("Song of %d %s by %s",
                this.getReleaseYear(), this.getTitle(), this.getArtist());
    }
}
