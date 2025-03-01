package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

public class CommandProcessor {
    // Default file path for the music library data
    private static String libraryFile = MusicLibraryFileHandler.getDefaultFile();

    // Stack to keep track of currently sourcing files
    private static final Stack<String> sourcingStack = new Stack<>();

    // Static variable to hold the MusicLibrary instance
    private static MusicLibrary library;

    public static void processCommands(MusicLibrary library) {
        MusicLibraryFileHandler.loadLibrary(MusicLibraryFileHandler.getDefaultFile());
        CommandProcessor.library = library;
        processCommand("SOURCE");
    }

    // Method to check if a command is a comment
    private static boolean isComment(String command) {
        return command.startsWith("#");
    }

    // Method to process a single command
    public static boolean processCommand(String command) {
        // Makes sure the command is not blank and is not a comment
        if (!command.isBlank() && !isComment(command)) {
            String[] actionAndArgs = command.split(" ", 2);
            String action = actionAndArgs[0]; // Extracts the action from the command
            switch (action) {
                case "ADD" -> {
                    if (actionAndArgs.length == 2) {
                        String[] typeAndParts = actionAndArgs[1].split(",");
                        if (typeAndParts.length != 7){
                            Message.send("Wrong number of elements: " + command + ".");
                            break;
                        }
                        MusicItem added = MusicItemFactory.createFromCSV(typeAndParts);
                        String addingOutput = library.addItem(added);
                        if (addingOutput != null){Message.send(addingOutput);}
                        else if (added != null && added.getInvalidFields().isEmpty()) {
                            library.addItem(added); // Adds the item to the library
                            Message.send(added.info() + " added to the library successfully.");
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);

                        } else if (added != null && !added.getInvalidFields().isEmpty()) {
                            Message.send("Invalid " + String.join(",", added.getInvalidFields()) + ": " + command);
                        } else {
                            Message.send("Wrong music item: " + command + ".");
                        }
                    } else {
                        Message.send("Invalid ADD command: " + command + ".");
                    }
                }
                case "CLEAR" -> {
                    if (actionAndArgs.length == 1){
                    if (library.getItems().isEmpty()){
                        Message.send("Music library is already empty.");
                        break;}
                    library.clearAllItems();
                    Message.send("Music library has been cleared successfully.");
                    MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile);}
                }
                case "EXIT" ->{
                    if (actionAndArgs.length == 1){
                        Message.send("Exiting program...");
                        return false;
                    }else{
                        Message.send("Invalid EXIT command: " + command + ".");}}
                case "LIST" ->
                    {
                        if (actionAndArgs.length == 1){
                            if (library.getItems().isEmpty()){
                                Message.send("The library is empty.");
                            }else{
                                library.listAllItems();
                            }
                        }else{
                            Message.send("Invalid LIST command: " + command + ".");}}
                case "LOAD" -> {
                    switch (actionAndArgs.length) {
                        case 1 -> {// If no file name is provided, load from the default file
                            Message.send("Loading from default library file.");
                            library.setItems(MusicLibraryFileHandler.loadLibrary(libraryFile));}
                        case 2 -> {
                            MusicLibrary temp = new MusicLibrary();
                            String fileName = actionAndArgs[1];
                            Message.send("Loading from file: " + actionAndArgs[1]);
                            temp.setItems(MusicLibraryFileHandler.loadLibrary(fileName));
                            if (!temp.getItems().isEmpty()){
                                libraryFile = fileName; // Updates the library file name
                                library = temp;
                            }
                        }
                        default ->
                            Message.send("Invalid arguments for LOAD.");
                    }
                }
                case "PAUSE" -> {
                    if (actionAndArgs.length == 1) {
                        // Check if an item is currently playing
                        if (library.getIsPlaying() != null) {
                            if (library.getIsPlaying().isPaused()){
                                Message.send(library.getIsPlaying().info() + "; is already on pause.");
                                break;
                            }
                            library.pauseItem(); // Pauses the currently playing item
                            Message.send("Pausing " + library.getIsPlaying().info() + ".");
                        } else {
                            Message.send("No item to pause.");
                        }
                    } else {
                        Message.send("Invalid PAUSE command: " + command + ".");
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
                                Message.send("Invalid PLAY command: " + String.join(" ", actionAndArgs) + ".");
                            } else {
                                play(library.getSearchedItem());
                            }
                        } else {
                            Message.send(library.getIsPlaying().info() + " is already playing.");
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
                            Message.send("Invalid ID for REMOVE command: " + actionAndArgs[1] + ".");
                        }
                    } else {
                        Message.send("Invalid REMOVE command: " + String.join(" ", actionAndArgs) + ".");
                    }
                }

                case "SAVE" -> { // Case for saving the music library to a file
                    // Switch statement to handle different numbers of arguments
                    switch (actionAndArgs.length) {
                        case 1 -> {// If no file name is provided, save to the default file
                            Message.send("Saving to default library file.");
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), libraryFile); }
                        case 2 -> { // If a file name is provided, save to that file
                            Message.send("Saving to file: " + actionAndArgs[1] + ".");
                            MusicLibraryFileHandler.saveLibrary(library.getItems(), actionAndArgs[1]); // Saves the library to the specified file
                        }
                        default -> // If the number of arguments is invalid, send an error message
                            Message.send("Invalid arguments for SAVE.");
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
                                break;
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
                            Message.send("SEARCH item ID " + id + " failed; no such item."); // Item is not found by ID
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
                case "STOP" ->{
                    if (library.getIsPlaying() != null){
                        Message.send("No item to STOP.");
                    }else{
                    library.stopItem();
                }
                }
                default ->
                    Message.send("Unknown command: " + command + ".");
            }
        }
        return true;
    }

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
        // Check if the file is already being sourced in the current chain
        if (sourcingStack.contains(commandFileName)) {
            Message.send("Currently sourcing " + commandFileName + "; SOURCE ignored.");
            return;
        }

        // Try to read and process commands from the specified file
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + commandFileName + ".txt"))) {
            Message.send("Sourcing " + commandFileName + "...");
            sourcingStack.push(commandFileName); // Push the file name onto the stack
            String line;
            int lineNumber = 0;
            // Read the file line by line
            boolean process = true;
            ArrayList<SourcingException> errors = new ArrayList<>();
            while (process && (line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    process = processCommand(line);
                } catch (Exception e) {
                    errors.add(new SourcingException(lineNumber, commandFileName, e));
                }
            }
            sourcingStack.pop(); // Pop the file name from the stack after processing
            if (!errors.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Errors while sourcing " + commandFileName + ":\n");
                for (SourcingException error : errors) {
                    errorMessage.append("\t").append(error.toString()).append("\n");
                }
                Message.send(errorMessage.toString());
            }
        } catch (IOException e) {
            Message.send("Sourcing " + commandFileName + " failed; file not found");
        }
    }

    public static class SourcingException {
        private final int lineNumber;
        private final String fileName;
        private final Exception exception;

        public SourcingException(int lineNumber, String fileName, Exception exception) {
            this.lineNumber = lineNumber;
            this.fileName = fileName;
            this.exception = exception;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getFileName() {
            return fileName;
        }

        public Exception getException() {
            return exception;
        }

        @Override
        public String toString() {
            return "Error in file " + fileName + " at line " + lineNumber + ": " + exception.toString();
        }
    }
}