package models;

import java.util.ArrayList;

import ui.Message;

public abstract class MusicItem {

    private int id, releaseYear;
    private ArrayList<String> invalidFields = new ArrayList<>();


    public ArrayList<String> getInvalidFields() {
        return new ArrayList<>(invalidFields);
    }

    public void setId(int id) {
        if (id >= 1) {
            this.id = id;
            invalidFields.remove("ID");
        } else {
            invalidFields.add("ID");
        }
    }

    public void setReleaseYear(int releaseYear) {
        if (releaseYear >= 1825 && releaseYear <= 2025) {
            this.releaseYear = releaseYear;
            invalidFields.remove("release year");
        } else {
            invalidFields.add("release year");
        }
    }

    public void setTitle(String title) {
        if (title.strip().length() != 0){
            this.title = title;
            invalidFields.remove("title");
        }
        else{invalidFields.add("title");}
        
    }

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

    public void setInvalidFields(ArrayList<String> invalidFields) {
        this.invalidFields = new ArrayList<>(invalidFields);
    }
}
