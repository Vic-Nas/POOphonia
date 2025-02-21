package models;

import java.util.Arrays;

public class Podcast extends MusicItem {

    private int episodeNumber;
    private String host, topic;

    public Podcast(String[] parts) {
        this.setCommons(Arrays.copyOfRange(parts, 0, 3));
        this.host = parts[3];
        this.topic = parts[4];
        this.episodeNumber = Integer.parseInt(parts[5]);

    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
        return String.format("Podcast [ID=%d, Title=%s, Release Year=%d, Host=%s, Episode Number=%d, Topic=%s]",
                this.getId(), this.getTitle(), this.getReleaseYear(), this.getHost(), this.getEpisodeNumber(), this.getTopic());
    }
}
