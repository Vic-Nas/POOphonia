package models;

import ui.Message;

public abstract class MusicItem {

    private int id, releaseYear;
    private String title;
    private boolean isPlaying;
    private boolean isPaused;

    public void play() {
        this.isPlaying = true;

    }

    public void pause() {
        this.isPaused = true;
    }

    public void stop() {
        Message.send("Stopping " + this.info() + ".");
        this.isPlaying = false;
        this.isPaused = false;
    }

    @Override
    public abstract String toString();

    public abstract String toCSV();

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
    public abstract String info();

    public boolean isPaused() {
        return isPaused;
    }
}
