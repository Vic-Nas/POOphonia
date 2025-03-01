package models;

public final class Song extends MusicItem {

    private int duration;
    private String artist, genre;
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
        if (duration >= 1 && duration <= 36000) {
            this.duration = duration;
            invalidFields.remove("duration");
        } else {
            invalidFields.add("duration");
        }
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if (artist.strip().length() != 0){
            this.artist = artist;
            invalidFields.remove("artist");
        }
        else{invalidFields.add("artist");}
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre.strip().length() != 0){
            this.genre = genre;
            invalidFields.remove("genre");
        }
        else{invalidFields.add("genre");}
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
