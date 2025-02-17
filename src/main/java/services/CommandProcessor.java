package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

public class CommandProcessor {

    private static boolean isComment(String command) {
        return command.startsWith("#");
    }
    private static String libraryFile = MusicLibraryFileHandler.getDefaultFile();
    private static String sourcing;

    protected static MusicLibrary library;
    private static MusicItem searchedItem;

    private static void add(String[] actionAndArgs) {
        MusicItem added = MusicItemFactory.createFromCSV(actionAndArgs[1].split(","));
        if (added != null) {
            library.addItem(added);
            Message.send(added.info() + " added to the library successfully.");
            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
        } else {
            Message.send("Invalid arguments for ADD.");
        }
    }

    private static void exit() {
        Message.send("***** POOphonia: Goodbye! *****");
        System.exit(0);
    }

    private static void pause() {
        if (library.getIsPlaying() != null) {
            library.pauseItem();
            Message.send("Paused " + library.getIsPlaying().info() + ".");
        } else {
            Message.send("No item is currently playing.");
        }
    }

    private static void play(MusicItem item) {
        if (library.getIsPlaying() != null) {
            stop();
        }

        library.playItem(item);
        Message.send("Playing " + item.info() + ".");
    }

    private static void stop() {
        if (library.getIsPlaying() != null) {
            library.stopItem();
        } else {
            Message.send("No item is currently playing.");
        }
    }

    private static void source(String commandFileName) {
        if (sourcing == null) {
            try (BufferedReader reader = new BufferedReader(new FileReader("data/" + commandFileName + ".txt"))) {
                Message.send("Sourcing " + commandFileName + "...");
                sourcing = commandFileName;
                String line;
                while ((line = reader.readLine()) != null) {
                    processCommand(line);
                }
                sourcing = null;
            } catch (IOException e) {
                Message.send("Sourcing" + commandFileName + " failed; file not found");
            }
        } else {
            Message.send("Currently sourcing " + sourcing + "; SOURCE Ignored.");
        }
    }

    public static void processCommand(String command) {
        if (command.isBlank() || !isComment(command)) {
        } else {
            String[] actionAndArgs = command.split(" ", 2);
            String action = actionAndArgs[0];
            switch (action) {
                case "ADD" -> {
                    if (actionAndArgs.length == 2) {
                        add(actionAndArgs);
                    } else {
                        Message.send("Invalid arguments for ADD.");
                    }
                }
                case "CLEAR" -> {
                    library.clearAllItems();
                    Message.send("Music library has been cleared successfully.");
                    MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
                }
                case "EXIT" ->
                    exit();
                case "LIST" ->
                    library.listAllItems();
                case "LOAD" -> {
                    switch (actionAndArgs.length) {
                        case 1 ->
                            MusicLibraryFileHandler.loadLibrary(libraryFile);
                        case 2 -> {
                            MusicLibraryFileHandler.loadLibrary(actionAndArgs[1]);
                            libraryFile = actionAndArgs[1];
                        }
                        default ->
                            Message.send("Invalid arguments for LOAD.");
                    }
                }
                case "PAUSE" -> {
                    if (actionAndArgs.length == 1) {
                        pause();
                    } else {
                        Message.send("Invalid arguments for PAUSE.");
                    }
                }
                case "PLAY" -> {
                    if (actionAndArgs.length == 2) {
                        String[] playArgs = actionAndArgs[1].split(" by ");
                        if (playArgs.length == 2) {
                            MusicItem item = library.searchItem(playArgs[0], playArgs[1]);
                            if (item == null) {
                                Message.send("PLAY item: " + actionAndArgs[1] + " failed; no such item.");
                            } else {
                                play(item);
                            }
                        } else {
                            try {
                                int id = Integer.parseInt(actionAndArgs[1]);
                                MusicItem item = library.searchItem(id);
                                if (item == null) {
                                    Message.send("PLAY item ID " + id + " failed; no such item.");
                                } else {
                                    play(item);
                                }
                            } catch (NumberFormatException e) {
                                Message.send("Invalid arguments for PLAY.");
                            }
                        }
                    } else {
                        if (library.getIsPlaying() == null) {
                            if (searchedItem == null) {
                                Message.send("Invalid PLAY command: " + String.join(" ", actionAndArgs));
                            } else {
                                play(searchedItem);
                            }
                        } else {
                            Message.send(library.getIsPlaying().info() + "is already playing.");
                        }
                    }
                }

                case "REMOVE" -> {
                    if (actionAndArgs.length == 2) {
                        try {
                            int id = Integer.parseInt(actionAndArgs[1]);
                            MusicItem itemToRemove = library.searchItem(id);
                            if (itemToRemove != null) {
                                library.removeItem(itemToRemove);
                                Message.send("Removed " + itemToRemove.info() + " successfully.");
                                MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
                            } else {
                                Message.send("REMOVE item" + id + " failed; no such item.");
                            }
                        } catch (NumberFormatException e) {
                            Message.send("Invalid ID for REMOVE command: " + actionAndArgs[1]);
                        }
                    } else {
                        Message.send("Invalid REMOVE command: " + String.join(" ", actionAndArgs));
                    }
                }

                case "SAVE" -> {
                    switch (actionAndArgs.length) {
                        case 1 ->
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
                        case 2 -> {
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), actionAndArgs[1]);
                        }
                        default ->
                            Message.send("Invalid arguments for SAVE.");
                    }
                }

                case "SEARCH" -> {
                    if (library.getIsPlaying() == null) {
                        if (actionAndArgs.length == 2) {
                            String[] searchArgs = actionAndArgs[1].split(" by ");
                            if (searchArgs.length == 2) {
                                searchedItem = library.searchItem(searchArgs[0], searchArgs[1]);
                            } else {
                                try {
                                    int id = Integer.parseInt(actionAndArgs[1]);
                                    searchedItem = library.searchItem(id);
                                } catch (NumberFormatException e) {
                                    Message.send("Invalid SEARCH format. Use 'SEARCH <id>' or 'SEARCH <title> by <artist>.'");
                                    return;
                                }
                            }

                            if (searchedItem != null) {
                                Message.send(searchedItem.info() + " is ready to PLAY.");
                            } else {
                                Message.send("SEARCH " + actionAndArgs[1] + " failed; no item found.");
                            }
                        } else {
                            Message.send("Invalid SEARCH command: " + String.join(" ", actionAndArgs) + ".");
                        }
                    } else {
                        Message.send(library.getIsPlaying().toString());
                    }
                }

                case "SOURCE" -> {
                    switch (actionAndArgs.length) {
                        case 1 ->
                            source(MusicLibraryFileHandler.getDefaultCommandFile());
                        case 2 ->
                            source(actionAndArgs[1]);
                        default ->
                            Message.send("Invalid arguments for SOURCE.");
                    }
                    break;
                }
                case "STOP" ->
                    stop();
                default ->
                    Message.send("Unknown operation.");
            }
        }
    }
    private final static ArrayList<String> startingCommands = new ArrayList<>(Arrays.asList("LOAD", "SOURCE"));

    public static void processCommands(MusicLibrary library) {

        CommandProcessor.library = library;
        for (String command : startingCommands) {
            processCommand(command);
        }
    }

}
