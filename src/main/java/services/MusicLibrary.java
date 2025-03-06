package services;

import java.util.ArrayList;
import java.util.List;

import models.Album;
import models.MusicItem;
import models.Song;
import ui.Message;

public class MusicLibrary {

    private ArrayList<MusicItem> items;
    private MusicItem searchedItem;
    private MusicItem isPlaying = null;

    public MusicLibrary(Iterable<MusicItem> items) {
        this.items = (ArrayList<MusicItem>) items; 
    }
    public MusicLibrary() {
        this.items = (ArrayList<MusicItem>) MusicLibraryFileHandler.loadLibrary(
            MusicLibraryFileHandler.getDefaultFile()); 
    }
    public ArrayList<MusicItem> getItems() {
        return new ArrayList<>(items);
    }

    public MusicItem getIsPlaying() {
        return isPlaying;
    }

    public String addItem(MusicItem item) {
        for (MusicItem m : this.items) {
            if (m.info().equals(item.info())) {
                return "ADD " + item.info() + " failed; item already in the library.";
            } else if (m.getId() == item.getId()) {
                return "ID " + item.getId() + " is already used.";
            }
        }
        items.add(item);
        return null;
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
        if (item.isPlaying()){
            isPlaying = null;
        }
    }

    @Override
    public String toString() {
        List<String> itemStrings = new ArrayList<>();
        for (MusicItem item : items) {
            itemStrings.add(item.toString());
        }
        return "Library:\n" + String.join("\n", itemStrings);
    }

    public void listAllItems() {
        Message.send(this.toString());
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
}
