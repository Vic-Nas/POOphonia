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

    // Method to check if a command is a comment
    private static boolean isComment(String command) {
        return command.startsWith("#");
    }

    // Default file path for the music library data
    private static String libraryFile = MusicLibraryFileHandler.getDefaultFile();

    // Variable to store the name of the file currently being sourced
    private static String sourcing;

    // Static variable to hold the MusicLibrary instance
    protected static MusicLibrary library;

    // Method to play a music item
    private static void play(MusicItem item) {
        // If an item is currently playing, stop it
        if (library.getIsPlaying() != null) {
            library.stopItem(); // Stops the currently playing item
        }

        library.playItem(item);
        Message.send("Playing " + item.info() + ".");
    }

    // Method to source commands from a file
    private static void source(String commandFileName) {
        // Check if sourcing is not already in progress
        if (sourcing == null) {
            // Try to read and process commands from the specified file
            try (BufferedReader reader = new BufferedReader(new FileReader("data/" + commandFileName + ".txt"))) {
                Message.send("Sourcing " + commandFileName + "...");
                sourcing = commandFileName; // Sets the sourcing variable to the current file name
                String line;
                // Read the file line by line
                while ((line = reader.readLine()) != null) {
                    if (line.strip().equals("EXIT")) {
                        break;
                    }
                    processCommand(line);
                }
                sourcing = null; // Resets the sourcing variable after processing the file
            } catch (IOException e) {
                Message.send("Sourcing" + commandFileName + " failed; file not found");
            }
        } else {
            Message.send("Currently sourcing " + sourcing + "; SOURCE Ignored.");
        }
    }

    // Method to process a single command
    public static void processCommand(String command) {
        // Makes sure the command is not blank and is not a comment
        if (!command.isBlank() && !isComment(command)) {
            String[] actionAndArgs = command.split(" ", 2);
            String action = actionAndArgs[0]; // Extracts the action from the command
            switch (action) {
                case "ADD" -> {
                    if (actionAndArgs.length == 2) {
                        MusicItem added = MusicItemFactory.createFromCSV(actionAndArgs[1].split(",")); // Creates a MusicItem from the CSV string
                        if (added != null) {
                            library.addItem(added); // Adds the item to the library
                            Message.send(added.info() + " added to the library successfully."); // Sends a success message
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile); // Saves the updated library to the file

                        } else {
                            Message.send("Invalid arguments for ADD."); // Sends an error message if the arguments are invalid
                        }
                    } else {
                        Message.send("Invalid arguments for ADD."); // Sends an error message if the arguments are invalid
                    }
                }
                case "CLEAR" -> {
                    library.clearAllItems();
                    Message.send("Music library has been cleared successfully.");
                    MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
                }
                case "EXIT" ->
                    Message.send("Invalid arguments for EXIT.");
                case "LIST" ->
                    library.listAllItems();
                case "LOAD" -> {
                    switch (actionAndArgs.length) {
                        case 1 -> // If no file name is provided, load from the default file
                            MusicLibraryFileHandler.loadLibrary(libraryFile);
                        case 2 -> { // If a file name is provided, load from that file
                            MusicLibraryFileHandler.loadLibrary(actionAndArgs[1]);
                            libraryFile = actionAndArgs[1]; // Updates the library file name
                        }
                        default ->
                            Message.send("Invalid arguments for LOAD.");
                    }
                }
                case "PAUSE" -> {
                    if (actionAndArgs.length == 1) {
                        // Check if an item is currently playing
                        if (library.getIsPlaying() != null) {
                            library.pauseItem(); // Pauses the currently playing item
                            Message.send("Paused " + library.getIsPlaying().info() + ".");
                        } else {
                            Message.send("No item is currently playing.");
                        }
                    } else {
                        Message.send("Invalid arguments for PAUSE.");
                    }
                }
                case "PLAY" -> {

                    if (actionAndArgs.length == 2) {
                        String[] playArgs = actionAndArgs[1].split(" by "); // Splits the arguments into title and artist
                        // If both title and artist are provided
                        if (playArgs.length == 2) {
                            MusicItem item = library.searchItem(playArgs[0], playArgs[1]); // Searches for the item by title and artist
                            if (item == null) {
                                Message.send("PLAY item: " + actionAndArgs[1] + " failed; no such item.");
                            } else {
                                play(item); // Plays the found item
                            }
                        } else {
                            // If only an ID is provided
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
                        // If no arguments are provided, play the searched item or send an error message
                        if (library.getIsPlaying() == null) {
                            if (library.getSearchedItem() == null) {
                                Message.send("Invalid PLAY command: " + String.join(" ", actionAndArgs)); // Sends an error message if no item is searched
                            } else {
                                play(library.getSearchedItem());
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
                            // If the item is found
                            if (itemToRemove != null) {
                                library.removeItem(itemToRemove);
                                Message.send("Removed " + itemToRemove.info() + " successfully.");
                                MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);
                            } else {
                                Message.send("REMOVE item " + id + " failed; no such item.");
                            }
                        } catch (NumberFormatException e) {
                            Message.send("Invalid ID for REMOVE command: " + actionAndArgs[1]);
                        }
                    } else {
                        Message.send("Invalid REMOVE command: " + String.join(" ", actionAndArgs));
                    }
                }

                case "SAVE" -> { // Case for saving the music library to a file
                    // Switch statement to handle different numbers of arguments
                    switch (actionAndArgs.length) {
                        case 1 -> // If no file name is provided, save to the default file
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile); // Saves the library to the default file
                        case 2 -> { // If a file name is provided, save to that file
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), actionAndArgs[1]); // Saves the library to the specified file
                        }
                        default -> // If the number of arguments is invalid, send an error message
                            Message.send("Invalid arguments for SAVE."); // Sends an error message
                    }
                }

                case "SEARCH" -> {
                    MusicItem searchedItem; // Variable to hold the searched item
                    int id = -1; // Variable to hold the ID, initialized to -1
                    if (actionAndArgs.length == 2) {
                        String[] searchArgs = actionAndArgs[1].split(" by "); // Splits the arguments into title and artist
                        // If both title and artist are provided
                        if (searchArgs.length == 2) {
                            searchedItem = library.searchItem(searchArgs[0], searchArgs[1]); // Searches for the item by title and artist
                        } else {
                            // If only an ID is provided
                            try {
                                id = Integer.parseInt(actionAndArgs[1]);
                                searchedItem = library.searchItem(id);
                            } catch (NumberFormatException e) {
                                Message.send("Invalid SEARCH format. Use 'SEARCH <id>' or 'SEARCH <title> by <artist>.'");
                                return;
                            }
                        }

                        // If the item is found
                        if (searchedItem != null) {
                            // If no item is currently playing
                            if (library.getIsPlaying() == null) {
                                Message.send(searchedItem.info() + " is ready to PLAY.");
                            } else {
                                Message.send(searchedItem.toString());
                            }

                        } else if (id != -1) {
                            Message.send("SEARCH item " + id + " failed; no such item."); // Item is not found by ID
                        } else {
                            Message.send("SEARCH " + actionAndArgs[1] + " failed; no item found."); // Item is not found by title and artist
                        }
                    } else {
                        Message.send("Invalid SEARCH command: " + String.join(" ", actionAndArgs) + ".");
                    }
                }

                case "SOURCE" -> {
                    switch (actionAndArgs.length) {
                        case 1 -> // If no file name is provided, source from the default command file
                            source(MusicLibraryFileHandler.getDefaultCommandFile());
                        case 2 -> // If a file name is provided, source from that file
                            source(actionAndArgs[1]);
                        default ->
                            Message.send("Invalid arguments for SOURCE.");
                    }
                }
                case "STOP" ->
                    library.stopItem();
                default ->
                    Message.send("Unknown operation."); // Sends an error message
            }
        }
    }
    // List of commands to be executed at the start of the application
    private final static ArrayList<String> startingCommands = new ArrayList<>(Arrays.asList("LOAD", "SOURCE"));

    public static void processCommands(MusicLibrary library) {
        CommandProcessor.library = library;
        for (String command : startingCommands) {

            processCommand(command);
        }
    }

}
