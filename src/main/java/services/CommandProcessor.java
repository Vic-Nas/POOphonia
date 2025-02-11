/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

public class CommandProcessor {

    private static String libraryName = "POOphonia";

    private static MusicLibrary library;
    private static MusicItem searchedItem;
    private static ArrayList<String> ACTIONS = new ArrayList<>(
            Arrays.asList("ADD", "CLEAR", "EXIT", "LIST", "LOAD", "PAUSE",
                    "PLAY", "REMOVE", "SAVE", "SEARCH", "SOURCE", "STOP"));

    private static void play(MusicItem item, String badOut) {
        if (item != null) {
            if (library.getIsPlaying() == null) {
                library.playItem(item);
                Message.send("Playing " + item.info() + ".");
            } else {
                Message.send(library.getIsPlaying().info() + " is already playing.");
            }

        } else {
            Message.send(badOut);
        }
    }

    private static MusicLibrary load(String libraryName, String badOut) {
        MusicLibrary loaded = new MusicLibrary();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + libraryName + ".csv"))) {
            String line;
            String[] parts;
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                MusicItem read = MusicItemFactory.createFromCSV(parts);
                loaded.addItem(read);
            }
        } catch (IOException e) {
            // Message.send("Error reading library " + libraryName + ".");
            Message.send(badOut);
        }
        return loaded;
    }

    private static void save() {
        final ArrayList<String> items = new ArrayList<>();
        for (MusicItem item : library.getItems()) {
            items.add(item.to_csv());
        }
        String csv = String.join("\n", items);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + libraryName + ".csv"))) {
            writer.write(csv);
            Message.send("Library saved successfully to " + libraryName);
        } catch (IOException e) {
            Message.send("Error saving library " + libraryName + ": " + e.getMessage());
        }
    }

    public static void processCommand(String command) {
        if (!command.isBlank() && !command.startsWith("#")) {
            String[] actionAndArgs = command.split(" ", 2);
            String action = actionAndArgs[0];
            switch (action) {
                case "ADD" -> {
                    MusicItem added = MusicItemFactory.createFromCSV(actionAndArgs[1].split(","));
                    if (added != null) {
                        library.addItem(added);
                        Message.send(added.info() + " added to the library sucessfully.");
                        save();
                    } else {
                        Message.send("Invalid arguments for ADD.");
                    }
                }
                case "CLEAR" -> {
                    library.clearAllItems();
                    Message.send("Music library has been cleared successfully.");

                    save();
                }
                case "EXIT" -> {
                    Message.send("***** POOphonia: Goodbye! *****");
                    System.exit(0);
                }
                case "LIST" -> {
                    library.listAllItems();
                }
                case "LOAD" -> {
                    switch (actionAndArgs.length) {
                        case 1 ->
                            processCommand("LOAD POOphonia");
                        case 2 -> {
                            library = load(actionAndArgs[1], "LOAD " + actionAndArgs[1] + " failed.");
                            Message.send("Library in file " + actionAndArgs[1] + " loaded successfully.");
                        }
                        default ->
                            Message.send("Invalid arguments for LOAD.");
                    }
                }

                case "PAUSE" -> {
                    if (library.getIsPlaying() != null) {
                        library.pauseItem();
                        Message.send("Paused " + library.getIsPlaying().info() + ".");
                    } else {
                        Message.send("No item is currently playing.");
                    }
                }
                case "PLAY" -> {
                    if (actionAndArgs.length != 1) {
                        String[] playArgs = actionAndArgs[1].split(" by ");
                        if (playArgs.length == 2) {
                            MusicItem item = library.searchItem(playArgs[0], playArgs[1]);
                            play(item, "PLAY item: " + actionAndArgs[1] + " failed; no such item.");
                        } else {
                            try {
                                int id = Integer.parseInt(actionAndArgs[1]);
                                MusicItem item = library.searchItem(id);
                                play(item, "PLAY item ID " + id + " failed; no such item.");
                            } catch (NumberFormatException e) {
                                Message.send("Invalid arguments for PLAY.");
                            }
                        }
                    } else {
                        play(searchedItem, "No item searched");
                    }
                }
                case "REMOVE" -> {
                    try {
                        int id = Integer.parseInt(actionAndArgs[1]);
                        MusicItem item = library.searchItem(id);
                        if (item != null) {
                            library.removeItem(item);
                            Message.send("Removed " + item.info() + " successfully.");
                            save();
                        } else {
                            Message.send("REMOVE item ID " + id + " failed; no such item.");
                        }
                    } catch (NumberFormatException e) {
                        Message.send("Invalid arguments for REMOVE.");
                    }
                }
                case "SAVE" -> {
                    switch (actionAndArgs.length) {
                        case 1 ->
                            save();
                        case 2 -> {
                            libraryName = actionAndArgs[1];
                            save();
                        }
                        default ->
                            Message.send("Invalid arguments for SAVE.");
                    }
                }
                case "SEARCH" -> {
                    if (actionAndArgs.length == 2) {
                        String[] searchArgs = actionAndArgs[1].split(" by ");
                        if (searchArgs.length == 2) {
                            MusicItem item = library.searchItem(searchArgs[0], searchArgs[1]);
                            if (item != null) {
                                searchedItem = item;
                                if (library.getIsPlaying() == null) {
                                    Message.send(item.info() + " is ready to PLAY.");
                                } else {
                                    Message.send(item.toString());
                                }
                            } else {
                                Message.send("SEARCH " + actionAndArgs[1] + " failed. No item found.");
                            }
                        } else {
                            try {
                                int id = Integer.parseInt(actionAndArgs[1]);
                                MusicItem item = library.searchItem(id);
                                if (item != null) {
                                    searchedItem = item;
                                    if (library.getIsPlaying() == null) {
                                        Message.send(item.info() + " is ready to PLAY.");
                                    } else {
                                        Message.send(item.toString());
                                    }
                                } else {
                                    Message.send("SEARCH item ID " + id + " failed; no such item.");
                                }
                            } catch (NumberFormatException e) {
                                Message.send("Invalid arguments for SEARCH.");
                            }
                        }
                    } else {
                        Message.send("Invalid arguments for SEARCH.");
                    }
                }
                case "SOURCE" -> {
                    // Implement the SOURCE functionality
                    Message.send("SOURCE functionality not implemented yet.");
                }
                case "STOP" -> {
                    if (library.getIsPlaying() != null) {
                        library.stopItem();
                        Message.send("Stopped playing.");
                    } else {
                        Message.send("No item is currently playing.");
                    }
                    // Implement the LOAD functionality
                    Message.send("LOAD functionality not implemented yet.");
                }
                default ->
                    Message.send("Unknown operation.");
            }
        }

    }

    public static void processCommands(MusicLibrary library) {
        CommandProcessor.library = library;
        processCommand("LOAD");
        Message.send("Sourcing commands...");
        try (BufferedReader reader = new BufferedReader(new FileReader("data/commands.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processCommand(line);
            }
        } catch (IOException e) {
            Message.send("Error reading commands file: " + e.getMessage());
        }
    }
}
