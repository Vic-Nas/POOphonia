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

    protected void setCommons(String[] parts) {
        this.id = Integer.parseInt(parts[0]);
        this.title = parts[1];
        this.releaseYear = Integer.parseInt(parts[2]);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public abstract String info();

    public boolean isPaused() {
        return isPaused;
    }
}
