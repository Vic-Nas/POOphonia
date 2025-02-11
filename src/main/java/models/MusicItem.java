package models;

public abstract class MusicItem {

    private int id, releaseYear;
    private String title;
    private boolean isPlaying;

    public void play() {
        this.isPlaying = true;

    }

    public void pause() {

    }

    public void stop() {
        this.isPlaying = false;
    }

    @Override
    public abstract String toString();

    public abstract String to_csv();

    public boolean toCSV() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    // public void setReleaseYear(int releaseYear) {
    //     this.releaseYear = releaseYear;
    // }
    protected void setElements(String[] parts) {
        this.id = Integer.parseInt(parts[0]);
        this.title = parts[1];
        this.releaseYear = Integer.parseInt(parts[2]);
    }

    public int getId() {
        return id;
    }

    // public void setId(int id) {
    //     this.id = id;
    // }
    public String getTitle() {
        return title;
    }

    // public void setTitle(String title) {
    //     this.title = title;
    // }
    public boolean isPlaying() {
        return isPlaying;
    }

    // public void setIsPlaying(boolean isPlaying) {
    //     this.isPlaying = isPlaying;
    // }
}
