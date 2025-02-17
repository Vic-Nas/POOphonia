package services;

import java.util.ArrayList;

import models.Album;
import models.MusicItem;
import models.Song;
import ui.Message;

public class MusicLibrary {

    private ArrayList<MusicItem> items = new ArrayList<>();

    public ArrayList<MusicItem> getItems() {
        return new ArrayList<>(items);
    }

    private MusicItem isPlaying = null;

    public MusicItem getIsPlaying() {
        return isPlaying;
    }

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

    public MusicItem searchItem(String title, String artist) {
        for (MusicItem item : items) {
            if (item instanceof Song) {
                Song song = (Song) item;
                if (title.equals(song.getTitle()) && artist.equals(song.getArtist())) {
                    return song;
                }
            } else if (item instanceof Album) {
                Album album = (Album) item;
                if (title.equals(album.getTitle()) && artist.equals(album.getArtist())) {
                    return album;
                }
            }
        }
        return null;
    }

    public void removeItem(MusicItem item) {
        items.remove(item);
    }

    public void listAllItems() {
        Message.send("Library:");
        String part1, part2 = "";
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

}
