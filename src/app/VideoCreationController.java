package app;

import com.jfoenix.controls.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import processes.*;
import java.util.ArrayList;

public class VideoCreationController {

    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton reviewButton;
    @FXML
    private JFXButton quitButton;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXTextField videoNameField;
    @FXML
    private JFXSlider numImages;
    @FXML
    private JFXListView<String> textListView;
    @FXML
    private JFXComboBox<String> voiceChoiceBox;
    @FXML
    private JFXButton addTextButton;
    @FXML
    private JFXButton removeTextButton;
    @FXML
    private JFXButton moveTextUpButton;
    @FXML
    private JFXButton moveTextDownButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label searchLabel;
    @FXML
    private JFXTextArea textArea;

    private ObservableList<String> chosenTextItems;
    private String currentSearch = "banana";
    private JFXDialog dialog;

    @FXML
    private void searchWiki() {
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.isEmpty()) return;

        JFXDialog dialog = loadingDialog("Searching for " + searchTerm + "...");

        Task<ArrayList<String>> search = new SearchWiki(searchTerm, textArea);
        search.setOnSucceeded(e -> {
            searchLabel.setText("You searched for: " + searchTerm + "\n");
            dialog.close();
        });
        Thread thread = new Thread(search);
        thread.start();
        currentSearch = searchTerm;
    }

    @FXML
    private void createVideo() {

        // If no text is selected then raise an error
//        if (textListView.getSelectionModel().getSelectedItems().size() == 0) {
//            alertCreator("Selection Process", "Invalid Selection", "Please select some text.");
//            return;
//        }

        // Error checking for empty/null selected
//        String customName = videoNameField.getText();
//        String searchTerm = searchField.getText();
//        if (customName == null || customName.isEmpty()) return;
//        if (searchTerm == null || searchTerm == "") return;
    	
        dialog = new DialogBuilder().loadingDialog(stackPane, "Creating Video");
    	createAudio();
    }

    private void createAudio() {
    	Task createAudiosTask = new CreateAudios(textListView.getItems(), voiceChoiceBox.getSelectionModel().getSelectedItem());
    	createAudiosTask.setOnSucceeded(e-> {;
    		stitchAudio((ArrayList<String>) createAudiosTask.getValue());
    	});
    	Thread thread = new Thread(createAudiosTask);
    	thread.start();
    }

    private void stitchAudio(ArrayList<String> audioFiles) {
    	Task stitchAudioTask = new StitchAudio(audioFiles);
    	stitchAudioTask.setOnSucceeded(e-> combineAudioVideo());
    	Thread thread = new Thread(stitchAudioTask);
    	thread.start();
    }

    private void combineAudioVideo() {
        String videoName = videoNameField.getText();
        Double val = numImages.getValue();
        String finNumImages = Integer.toString(val.intValue());

        Task<ArrayList<String>> videoCreation = new CreateVideo(currentSearch, finNumImages, videoName);
        videoCreation.setOnSucceeded(e-> {
            dialog.close();
            VideoManager.getVideoManager().add(new VideoCreation(videoName, currentSearch, (int) numImages.getValue()));    
            new DialogBuilder().closeDialog(stackPane, "Video Creation Successful!", videoName + " was created.");
        });
        Thread video = new Thread(videoCreation);
        video.start();
    }

    @FXML
    private void add() {
        // Error checking for empty/null selected
        String selectedText = selectedText();
        if (selectedText == null || selectedText.isEmpty()) return;
        if (countWords(selectedText) > 40) {
        	new DialogBuilder().closeDialog(stackPane, "Invalid selection", "Please select less than 40 words");
            return;
        }
        // Add selected as a line in listview
        textListView.getItems().add(selectedText);
    }

    @FXML
    private void remove() {
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        textListView.getItems().remove(selected);
    }

    @FXML
    private void moveUp() {
        int index= textListView.getSelectionModel().getSelectedIndex();
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (index < 1 | selected == null) return;
        // Now swap items
        String tempString = textListView.getItems().get(index-1);
        chosenTextItems.set(index, tempString);
        chosenTextItems.set(index-1, selected);
        textListView.getSelectionModel().select(index-1);
    }

    @FXML
    private void moveDown() {
        int index= textListView.getSelectionModel().getSelectedIndex();
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (index >= textListView.getItems().size() -1 | selected == null) return;
        // Now swap items
        String tempString = textListView.getItems().get(index+1);
        chosenTextItems.set(index, tempString);
        chosenTextItems.set(index+1, selected);
        textListView.getSelectionModel().select(index+1);
    }

    @FXML
    private void quit() {
    	searchLabel.getScene().getWindow().hide();
    }
    
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	updateVoiceList();
    }

    private void updateVoiceList() {
    	Task<ArrayList<String>> listVoices = new ListVoices();
    	listVoices.setOnSucceeded(e -> {
    		voiceChoiceBox.setItems(FXCollections.observableArrayList(listVoices.getValue()));
    		voiceChoiceBox.getSelectionModel().select(0);
    	});
        Thread thread = new Thread(listVoices);
        thread.start();
    }

    @FXML
    private void describeSearchResults() {
        Describe desc = new Describe("description here");
        desc.describeSearchResults();
    }

    @FXML
    private void describeAddRemoveText() {
        Describe desc = new Describe("description here");
        desc.describeAddRemoveText();
    }

    @FXML
    private void describeShuffleText() {
        Describe desc = new Describe("description here");
        desc.describeShuffleText();
    }

    @FXML
    private void describeBackButton() {
        Describe desc = new Describe("description here");
        desc.describeBackButton();
    }

    @FXML
    private void describeSearchFunction() {
        Describe desc = new Describe("description here");
        desc.describeSearchFunction();
    }

    @FXML
    private void describeChooseVoice() {
        Describe desc = new Describe("description here");
        desc.describeChooseVoice();
    }

    @FXML
    private void describeVideoNameField() {
        Describe desc = new Describe("description here");
        desc.describeVideoNameField();
    }

    @FXML
    private void describeImagesSlider() {
        Describe desc = new Describe("description here");
        desc.describeImagesSlider();
    }

    @FXML
    private void describeCreateButton() {
        Describe desc = new Describe("description here");
        desc.describeCreateButton();
    }

    @FXML
    private void describeTextList() {
        Describe desc = new Describe("description here");
        desc.describeTextList();
    }

    private int countWords(String input) {
        if (input == null || input.isEmpty()) {
          return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
      }
    
    private String selectedText() {
    	String[] processString = textArea.getSelectedText().split("\t");
    	String processedText = "";
    	for (String line: processString) {
        	String woo = line.replaceAll("^\\d+|\\d+$", ""); 
    		processedText += woo;
    	}
    	return processedText;
    }

    private JFXDialog loadingDialog(String title) {
    	JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        JFXSpinner spinner = new JFXSpinner();
        spinner.setPrefSize(50, 50);
        dialogContent.setBody(spinner);
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
        dialog.show();
        return dialog;
    }

}
