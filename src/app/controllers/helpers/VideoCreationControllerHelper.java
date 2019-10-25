package app.controllers.helpers;

import app.*;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;
import processes.CreateAudios;
import processes.CreateVideo;
import processes.ListVoices;
import processes.StitchAudio;

import java.util.ArrayList;

/**
 * Class providing helper methods for the VideoCreationController class
 */
public class VideoCreationControllerHelper {

    // Field declarations
    private VideoManager videoManager = VideoManager.getVideoManager();

    /**
     * Constructor provided so VideoCreationController class can gain non-static access to the methods in this class
     */
    public VideoCreationControllerHelper() {

    }

    /**
     * Automatically suggest a name for the video creation upon search of wiki - appears in the input field for video name
     */
    public String autoName(String currentSearch) {
        int i = 0;
        String currentName = currentSearch + i;
        while (videoExists(currentName)){
            i++;
            currentName = currentSearch + i;
        }
        return currentName;
    }

    /**
     * Check if a video already exists with a certain name
     * @param name
     * @return true if such a video exists, and false if it doesn't
     */
    public boolean videoExists(String name) {
        for (VideoCreation v: videoManager.getVideos()) {
            if (v.getName().equals(name)) return true;
        }
        return false;
    }


    /**
     * Error checking of the text and video name before the creation of a video
     */
    public void checkCanMakeVideo(StackPane stackPane, JFXListView<String> textListView, JFXTextField videoNameField) {
        // If no text is selected then raise an error
        if (textListView.getItems().size() == 0) {
            new DialogBuilder().close(stackPane, "Invalid Text", "Please add some text to the list.");
            return;
        }
        // If the user includes a space in the video name, ask them to enter a different video name
        if (videoNameField.getText().contains(" ")) {
            new DialogBuilder().close(stackPane, "Invalid Video Name", "Whoops! You can't have spaces in your video's name- please pick another name.");
            return;
        }
    }

    /**
     * Error checking of the searched word before the creation of a video
     */
    public void checkSearchFieldEntry(StackPane stackPane, JFXTextField searchField, String currentSearch) {
        // If the user cheekily entered a different word in the search term box (but didn't click search) and tries to make a video, prevent the user from doing so as this new term will be associated with text from a different search term
        if (!currentSearch.equalsIgnoreCase(searchField.getText())) {
            new DialogBuilder().close(stackPane, "Invalid Text", "Complete this search before making a new video. \nOtherwise, change this new word back to the one you previously searched.");
            return;
        }
        // If the user searched a banned word, say no results were found and allow to retry
        for (String s : NaughtyWords.getRegularBadWordsList()) {
            if (searchField.getText().equals(s)) {
                new DialogBuilder().close(stackPane, "Invalid Search Term", "Whoops! Please pick another name");
                return;
            }
        }
    }

    /**
     * Refresh the dropdown list of voice options for the video creations
     */
    public void updateVoiceList(JFXComboBox<String> voiceChoiceBox) {
        Task<ArrayList<String>> listVoices = new ListVoices();
        listVoices.setOnSucceeded(e -> {
            voiceChoiceBox.setItems(FXCollections.observableArrayList(listVoices.getValue()));
            voiceChoiceBox.getSelectionModel().select(0);
        });
        Thread thread = new Thread(listVoices);
        thread.start();
    }

    /**
     * Count the number of words in the input
     * @return the number of words
     */
    public int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    /**
     * Format the selected text for use
     * @return the formatted text
     */
    public String selectedText(String[] processString) {
        String processedText = "";
        for (String line: processString) {
            String woo = line.replaceAll("^\\d+|\\d+$", "");
            processedText += woo;
        }
        return processedText;
    }

    /**
     * Enable or disable the move up and move down buttons based on whether there is text in the list of text pieces given
     */
    public void updateMoveButtons(JFXListView<String> textListView, JFXButton moveUpButton, JFXButton moveDownButton) {
        boolean isEmpty = textListView.getItems().size() == 0;
        moveUpButton.setDisable(isEmpty);
        moveDownButton.setDisable(isEmpty);
    }
}
