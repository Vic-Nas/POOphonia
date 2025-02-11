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

    public void removeItem(MusicItem item) {
        items.remove(item);
    }

    public void listAllItems() {
        for (MusicItem item : items) {
            Message.send(item.toString());
        }
    }

    public void playItem(int id) {
        for (MusicItem item : items) {
            if (item.getId() == id) {
                item.play();
                isPlaying = item;
                break;
            }
        }
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

}
