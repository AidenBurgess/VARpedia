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

    /**
     * Constructor provided so VideoCreationController class can gain non-static access to the methods in this class
     */
    public VideoCreationControllerHelper() {

    }

    /**
     * Automatically suggest a name for the video creation upon search of wiki - appears in the input field for video name
     */
    public String autoName(String currentSearch, VideoManager videoManager) {
        int i = 0;
        String currentName = currentSearch + i;
        while (videoExists(currentName, videoManager)){
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
    public boolean videoExists(String name, VideoManager videoManager) {
        for (VideoCreation v: videoManager.getVideos()) {
            if (v.getName().equals(name)) return true;
        }
        return false;
    }


    /**
     * Convert all text currently displayed in the text list to audio files
     */
    public void createAudio(String name, JFXListView<String> textListView, String videoName, Double val, String currentSearch, JFXDialog dialog, VideoManager videoManager, JFXSlider numImages, JFXButton createButton, StackPane stackPane) {
        // Find the original voice name
        String voice = Voice.findVoice(name);
        // Create audio
        Task createAudiosTask = new CreateAudios(textListView.getItems(), voice);
        createAudiosTask.setOnSucceeded(e-> {;
            stitchAudio((ArrayList<String>) createAudiosTask.getValue(), videoName, val, currentSearch, dialog, textListView, videoManager, numImages, createButton, stackPane);
        });
        Thread thread = new Thread(createAudiosTask);
        thread.start();
    }

    /**
     * Combine input audio files to make one final audio file, and then make the final video creation when this is done
     * @param audioFiles
     */
    private void stitchAudio(ArrayList<String> audioFiles, String videoName, Double val, String currentSearch, JFXDialog dialog, JFXListView<String> textListView, VideoManager videoManager, JFXSlider numImages, JFXButton createButton, StackPane stackPane) {
        Task stitchAudioTask = new StitchAudio(audioFiles);
        // When the audio file has been made, create the video by combining
        stitchAudioTask.setOnSucceeded(e-> combineAudioVideo(videoName, val, currentSearch, dialog, textListView, videoManager, numImages, createButton, stackPane));
        Thread thread = new Thread(stitchAudioTask);
        thread.start();
    }

    /**
     * Combine text, audio, and video to create the final video creation
     */
    private void combineAudioVideo(String videoName, Double val, String currentSearch, JFXDialog dialog, JFXListView<String> textListView, VideoManager videoManager, JFXSlider numImages, JFXButton createButton, StackPane stackPane) {
        // Retrieve selected number of images
        String finNumImages = Integer.toString(val.intValue());

        // Create the video, and notify the user when it's done
        Task<ArrayList<String>> videoCreation = new CreateVideo(currentSearch, finNumImages, videoName);
        videoCreation.setOnSucceeded(e-> {
            dialog.close();
            ArrayList<String> textContent = new ArrayList<>(textListView.getItems());
            VideoCreation video = new VideoCreation(videoName, currentSearch, (int) numImages.getValue(), textContent);
            videoManager.add(video);
            new DialogBuilder().close(stackPane, "Video Creation Successful!", videoName + " was created.");
            createButton.setDisable(false);
            autoName(currentSearch, videoManager);
        });
        Thread video = new Thread(videoCreation);
        video.start();
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
