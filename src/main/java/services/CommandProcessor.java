/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    private static MusicLibrary library;
    private static MusicItem searchedItem;
    private static ArrayList<String> ACTIONS = new ArrayList<>(
            Arrays.asList("ADD", "CLEAR", "EXIT", "LIST", "LOAD", "PAUSE",
                    "PLAY", "REMOVE", "SAVE", "SEARCH", "SOURCE", "STOP"));

    public static void processCommand(String command) {
        if (!command.isBlank() && !command.startsWith("#")) {
            String[] actionAndArgs = command.split(" ", 2);
            String action = actionAndArgs[0];
            switch (action) {
                case "ADD" -> {
                    MusicItem added = MusicItemFactory.createFromCSV(actionAndArgs[1].split(","));
                    if (added != null) {
                        library.addItem(added);
                    } else {
                        Message.send("Invalid arguments for ADD.");
                    }
                }
                case "CLEAR" -> {
                    library.clearAllItems();
                    Message.send("Library cleared.");
                }
                case "EXIT" -> {
                    Message.send("Exiting the program.");
                    System.exit(0);
                }
                case "LIST" -> {
                    library.listAllItems();
                }
                case "LOAD" -> {
                    // Implement the LOAD functionality
                    Message.send("LOAD functionality not implemented yet.");
                }
                case "PAUSE" -> {
                    // Implement the PAUSE functionality
                    Message.send("PAUSE functionality not implemented yet.");
                }
                case "PLAY" -> {
                    if (actionAndArgs.length != 1) {
                        String[] playArgs = actionAndArgs[1].split(" by ");
                        if (playArgs.length == 2) {
                            MusicItem item = library.searchItem(playArgs[1]);
                            if (item != null) {
                                library.playItem(item);
                            } else {
                                Message.send("Item not found.");
                            }
                        } else {
                            try {
                                int id = Integer.parseInt(actionAndArgs[1]);
                                MusicItem item = library.searchItem(id);
                                if (item != null) {
                                    library.playItem(item);
                                } else {
                                    Message.send("Item not found.");
                                }
                            } catch (NumberFormatException e) {
                                Message.send("Invalid arguments for PLAY.");
                            }
                        }
                    } else if (searchedItem != null) {
                        library.playItem(searchedItem);
                    } else {
                        Message.send("No item searched");
                    }
                }
                case "REMOVE" -> {
                    try {
                        int id = Integer.parseInt(actionAndArgs[1]);
                        MusicItem item = library.searchItem(id);
                        if (item != null) {
                            library.removeItem(item);
                            Message.send("Item removed.");
                        } else {
                            Message.send("Item not found.");
                        }
                    } catch (NumberFormatException e) {
                        Message.send("Invalid arguments for REMOVE.");
                    }
                }
                case "SAVE" -> {
                    // Implement the SAVE functionality
                    Message.send("SAVE functionality not implemented yet.");
                }
                case "SEARCH" -> {
                    if (actionAndArgs.length > 1) {
                        String[] searchArgs = actionAndArgs[1].split(" by ");
                        if (searchArgs.length == 2) {
                            MusicItem item = library.searchItem(searchArgs[1]);
                            if (item != null) {
                                Message.send(item.toString());
                            } else {
                                Message.send("Item not found.");
                            }
                        } else {
                            try {
                                int id = Integer.parseInt(actionAndArgs[1]);
                                MusicItem item = library.searchItem(id);
                                if (item != null) {
                                    searchedItem = item;
                                    Message.send(item.toString());
                                } else {
                                    Message.send("Item not found.");
                                }
                            } catch (NumberFormatException e) {
                                MusicItem item = library.searchItem(actionAndArgs[1]);
                                if (item != null) {
                                    Message.send(item.toString());
                                } else {
                                    Message.send("Item not found.");
                                }
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
                    // Implement the STOP functionality
                    Message.send("STOP functionality not implemented yet.");
                }
                default ->
                    Message.send("Unknown operation.");
            }
        }

    }

    public static void processCommands(MusicLibrary library) {
        CommandProcessor.library = library;

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
