package services;

import java.util.ArrayList;

import models.MusicItem;
import ui.Message;

public class MusicLibrary {

    private ArrayList<MusicItem> items = new ArrayList<>();

    private MusicItem isPlaying = null;

    public void addItem(MusicItem item) {
        items.add(item);
    }

    public MusicItem searchItem(int id) {
        for (MusicItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public MusicItem searchItem(String title) {
        for (MusicItem item : items) {
            if (title.equals(item.getTitle())) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(MusicItem item) {
        items.remove(item);
    }

    public void listAllItems() {
        for (MusicItem item : items) {
            Message.send(item.toString());
        }
    }

    public void playItem(MusicItem item) {
        item.play();
        isPlaying = item;
    }

    public void pauseItem() {
        isPlaying.pause();
    }

    public void stopItem() {
        isPlaying.stop();
        isPlaying = null;
    }

    public void clearAllItems() {
        items = new ArrayList<>();
        isPlaying = null;
    }

    public void save(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void playItem(String playArg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
