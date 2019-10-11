package app;

import com.jfoenix.controls.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import processes.*;
import java.util.ArrayList;

public class VideoCreationController {

    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXTextField videoNameField;
    @FXML
    private JFXSlider numImages;
    @FXML
    private JFXListView<String> textListView;
    @FXML
    private JFXComboBox<String> voiceChoiceBox;
    @FXML
    private JFXButton helpCreateButton;
    @FXML
    private JFXButton helpSearchResultsButton;
    @FXML
    private JFXButton helpSearchButton;
    @FXML
    private JFXButton helpAddRemoveButton;
    @FXML
    private JFXButton helpTextListButton;
    @FXML
    private JFXButton helpNumImagesButton;
    @FXML
    private JFXButton helpVideoNameButton;
    @FXML
    private JFXButton helpBackButton;
    @FXML
    private JFXButton helpVoicesButton;
    @FXML
    private JFXButton helpShuffleButton;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton createButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label searchLabel;
    @FXML
    private JFXTextArea textArea;

    private int wordLimit = 40;
    private ObservableList<String> chosenTextItems;
    private String currentSearch = "banana";
    private JFXDialog dialog;

    @FXML
    private void searchWiki() {
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.isEmpty()) return;
        textListView.getItems().clear();
        dialog = new DialogBuilder().loadingDialog(stackPane, "Searching for " + searchTerm + "...");

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
        if (textListView.getItems().size() == 0) {
            new DialogBuilder().closeDialog(stackPane, "Invalid Text", "Please add some text to the list.");
            return;
        }
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
        if (countWords(selectedText) > wordLimit) {
        	new DialogBuilder().closeDialog(stackPane, "Invalid selection", "Please select less than "+ wordLimit +" words");
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
        textListView.getItems().remove(selected);
        textListView.getItems().add((index-1), selected);
    }

    @FXML
    private void moveDown() {
        int index= textListView.getSelectionModel().getSelectedIndex();
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (index >= textListView.getItems().size() -1 | selected == null) return;
        textListView.getItems().remove(selected);
        textListView.getItems().add((index+1), selected);
    }

    @FXML
    private void quit() {
    	searchLabel.getScene().getWindow().hide();
    }
    
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	updateVoiceList();
    	setUpHelp();
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

    private void setUpHelp() {
        helpSearchResultsButton.setTooltip(new HoverToolTip("Your search results will appear here. \nYou can click and drag to select text, add text to the results by typing it in, or delete text you don't want to see!").getToolTip());

        helpAddRemoveButton.setTooltip(new HoverToolTip("After selecting some text from the left, press the \">\" button to add it to the list on the right. \nOr, after selecting one or more of the pieces of text from the right, click the \"<\" button to delete them from the list on the right!").getToolTip());

        helpShuffleButton.setTooltip(new HoverToolTip("After selecting a piece of text from the right, click the \"^\" or \"v\" buttons to move that piec up or down in the list!").getToolTip());

        helpBackButton.setTooltip(new HoverToolTip("Click this button to go back to the main menu!").getToolTip());

        helpSearchButton.setTooltip(new HoverToolTip("Type in the word you want to search the meaning for to the left of this button, then click this button to search it!").getToolTip());

        helpCreateButton.setTooltip(new HoverToolTip("After filling in the video name, number of images, and the voice you would like to use in the areas to the left, click this button to make your video!").getToolTip());

        helpVoicesButton.setTooltip(new HoverToolTip("In this list are different voices you can choose to speak your text in your video! \nClick it to see the options, then click one of the options shown to choose it!").getToolTip());

        helpVideoNameButton.setTooltip(new HoverToolTip("Type in what you want to name your final video here!").getToolTip());

        helpNumImagesButton.setTooltip(new HoverToolTip("Click and drag the dot along the line to choose how many picture you want to have in your video, from 1 to 10!").getToolTip());

        helpTextListButton.setTooltip(new HoverToolTip("This is where each bit of text is shown in a list! \nSelect one piece of text by clicking on it!").getToolTip());
    }

    @FXML
    private void checkValidSearch() {
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()) searchButton.setDisable(true);
        else searchButton.setDisable(false);
    }

    @FXML
    private void checkValidCreate() {
        String videoName = videoNameField.getText();
        if (videoName == null || videoName.trim().isEmpty() || videoName.contains(" ")) createButton.setDisable(true);
        else createButton.setDisable(false);
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

}
