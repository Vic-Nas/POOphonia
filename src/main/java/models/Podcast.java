package models;

import java.util.Arrays;

public class Podcast extends MusicItem {

    private int episodeNumber;
    private String host, topic;

    public Podcast(String[] parts) {
        this.setElements(Arrays.copyOfRange(parts, 0, 3));
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
    public String to_csv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return String.format("Podcast %s episode %d of %d on %s by %s", this.getTitle(),
                this.getEpisodeNumber(), this.getReleaseYear(), this.getTopic(), this.getHost());
    }
}
