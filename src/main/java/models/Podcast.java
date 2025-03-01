package models;

import java.util.ArrayList;

public final class Podcast extends MusicItem {

    private int episodeNumber;
    private String host, topic;

    public Podcast(String[] parts) {
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
        this.host = parts[3];
        this.topic = parts[4];
        try {
            this.setEpisodeNumber(Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            this.setEpisodeNumber(-1);
        }
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    private void setEpisodeNumber(int episodeNumber) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (episodeNumber >= 1 && episodeNumber <= 500) {
            this.episodeNumber = episodeNumber;
            invalidFields.remove("episode number");
        } else {
            invalidFields.add("episode number");
        }
        setInvalidFields(invalidFields);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (host.strip().length() != 0){
            this.host = host;
            invalidFields.remove("host");
        }
        else{invalidFields.add("host");}
        setInvalidFields(invalidFields);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        ArrayList<String> invalidFields = getInvalidFields();
        if (topic.strip().length() != 0){
            this.topic = topic;
            invalidFields.remove("topic");
        }
        else{invalidFields.add("topic");}
        setInvalidFields(invalidFields);
    }

    @Override
    public String toCSV() {
        return String.format("podcast,%d,%s,%d,%s,%s,%d", this.getId(), this.getTitle(),
                this.getReleaseYear(), this.getHost(), this.getTopic(), this.getEpisodeNumber());
    }

    @Override
    public String info() {
        return String.format("Podcast %s episode %d of %d on %s by %s",
                this.getTitle(), this.getEpisodeNumber(), this.getReleaseYear(), this.getTopic(), this.getHost());
    }

    @Override
    public String toString() {
        return String.format("Podcast [ID=%d, Title=%s, Release Year=%d, Host=%s, Episode=%d, Topic=%s]",
                this.getId(), this.getTitle(), this.getReleaseYear(), this.getHost(), this.getEpisodeNumber(), this.getTopic());
    }

    
}
