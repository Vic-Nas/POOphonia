package models;

import java.util.ArrayList;

public final class Song extends MusicItem {

    private int duration;
    private String artist, genre;
    private final int maxDuration = 36000;

    public Song(String[] parts) {
        try {
            this.setId(Integer.parseInt(parts[0]));
        } catch (NumberFormatException e) {
            this.setId(-1);
        }
        this.setTitle(parts[1]);
        try {
            this.setReleaseYear(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            this.setReleaseYear(-1);
        }

        this.setArtist(parts[3]);
        this.setGenre(parts[4]);
        try {
            this.setDuration(Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            this.setDuration(-1);
        }
    }

    @Override
    public String toCSV() {
        return String.format("song,%d,%s,%d,%s,%s,%d", this.getId(), this.getTitle(),
                this.getReleaseYear(), this.getArtist(), this.getGenre(), this.getDuration());
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (duration >= 1 && duration <= maxDuration) {
            this.duration = duration;
            invalidFields.remove("duration");
        } else {
            invalidFields.add("duration");
        }
        setInvalidFields(invalidFields);
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (artist.strip().length() != 0){
            this.artist = artist;
            invalidFields.remove("artist");
        }
        else{invalidFields.add("artist");}
        setInvalidFields(invalidFields);
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (genre.strip().length() != 0){
            this.genre = genre;
            invalidFields.remove("genre");
        }
        else{invalidFields.add("genre");}
        setInvalidFields(invalidFields);
    }

    @Override
    public String info() {
        return String.format("Song of %d %s by %s",
                this.getReleaseYear(), this.getTitle(), this.getArtist());
    }

    @Override
    public String toString() {
        return String.format("Song [ID=%d, Title=%s, Release Year=%d, Artist=%s, Genre=%s, Duration=%ds]",
                this.getId(), this.getTitle(), this.getReleaseYear(), this.getArtist(), this.getGenre(), this.getDuration());
    }


}
