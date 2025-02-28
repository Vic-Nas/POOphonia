package services;

import java.util.ArrayList;

import models.Album;
import models.MusicItem;
import models.Song;
import ui.Message;

public class MusicLibrary {

    private ArrayList<MusicItem> items = new ArrayList<>();
    private MusicItem searchedItem;

    public ArrayList<MusicItem> getItems() {
        return new ArrayList<>(items);
    }

    private MusicItem isPlaying = null;

    public MusicItem getIsPlaying() {
        return isPlaying;
    }

    public void addItem(MusicItem item) {
        for (MusicItem m : this.items) {
            if (m.info().equals(item.info())) {
                Message.send("ADD " + item.info() + " failed; item already in the library.");
                return;
            } else if (m.getId() == item.getId()) {
                Message.send("ID " + item.getId() + " is already used.");
                return;
            }
        }
        items.add(item);
    }

    public MusicItem searchItem(int id) {
        for (MusicItem item : items) {
            if (item.getId() == id) {
                this.searchedItem = item;
                return item;
            }
        }
        return null;
    }

    public MusicItem searchItem(String title, String artist) {
        for (MusicItem item : items) {
            if (item instanceof Song song && title.equals(song.getTitle()) && artist.equals(song.getArtist())) {
                this.searchedItem = song;
                return song;
            } else if (item instanceof Album album && title.equals(album.getTitle()) && artist.equals(album.getArtist())) {
                this.searchedItem = album;
                return album;
            }
        }
        return null;
    }

    public void removeItem(MusicItem item) {
        items.remove(item);
    }

    public void listAllItems() {
        Message.send("Library:");
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

    public void save(String fileName) {
        MusicLibraryFileHandler.saveLibrary(this.items, fileName);
    }

    public MusicItem getSearchedItem() {
        return searchedItem;
    }

    public void setItems(Iterable<MusicItem> items) {
        this.items = (ArrayList<MusicItem>) items; 
    }

}
