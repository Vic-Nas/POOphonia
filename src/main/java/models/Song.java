package models;

public class Song extends MusicItem {

    private int duration;
    private String artist, genre;

    public Song(String artist, int duration, String genre) {
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
