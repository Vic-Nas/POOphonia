package models;

public final class Album extends MusicItem {

    private int numberOfTracks;
    private String artist, label;

    public Album(String[] parts) {
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
        this.artist = parts[3];
        this.label = parts[4];
        try {
            this.setNumberOfTracks(Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            this.setNumberOfTracks(-1);
        }
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }
    
    public void setNumberOfTracks(int numberOfTracks) {
        if (numberOfTracks >= 1 && numberOfTracks <= 100) {
            this.numberOfTracks = numberOfTracks;
            invalidFields.remove("number of tracks");
        } else {
            invalidFields.add("number of tracks");
        }
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if (artist.strip().length() != 0){this.artist = artist;
            invalidFields.remove("artist");}
        else{invalidFields.add("artist");}
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label.strip().length() != 0){this.label = label;
            invalidFields.remove("label");}
        else{invalidFields.add("label");}
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
