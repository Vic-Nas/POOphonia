package models;

public final class Song extends MusicItem {

    private int duration;
    private String artist, genre;

    public Song(String[] parts) {
        try {
            this.setId(Integer.parseInt(parts[0]));
        } catch (NumberFormatException e) {
            this.setId(BAD_INT_VALUE);
        }
        this.setTitle(parts[1]);
        try {
            this.setReleaseYear(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            this.setReleaseYear(BAD_INT_VALUE);
        }

        this.artist = parts[3];
        this.genre = parts[4];
        try {
            this.setDuration(Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            this.setDuration(BAD_INT_VALUE);
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
        if (artist.strip().length() != 0){this.artist = artist;}
        else{invalidFields.add("artist");}
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre.strip().length() != 0){this.genre = genre;}
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
