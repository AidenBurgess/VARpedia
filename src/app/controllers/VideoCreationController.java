package app.controllers;

import app.*;
import com.jfoenix.controls.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import processes.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VideoCreationController extends DraggableWindow {

	// Root of scene
	@FXML private AnchorPane root;
	
	// Input fields for video creation
    @FXML private JFXTextField searchField;
    @FXML private JFXTextField videoNameField;
    @FXML private JFXSlider numImages;
    @FXML private JFXListView<String> textListView;
    @FXML private JFXComboBox<String> voiceChoiceBox;
    @FXML private JFXTextArea textArea;
	
	// Help "?" tooltip buttons
    @FXML private JFXButton helpCreateButton;
    @FXML private JFXButton helpSearchResultsButton;
    @FXML private JFXButton helpSearchButton;
    @FXML private JFXButton helpAddRemoveButton;
    @FXML private JFXButton helpTextListButton;
    @FXML private JFXButton helpNumImagesButton;
    @FXML private JFXButton helpVideoNameButton;
    @FXML private JFXButton helpBackButton;
    @FXML private JFXButton helpQuitButton;
    @FXML private JFXButton helpVoicesButton;
    @FXML private JFXButton helpShuffleButton;
	
	// Main function buttons
    @FXML private JFXButton searchButton;
    @FXML private JFXButton createButton;
	
	// Dialogue and labels
    @FXML private StackPane stackPane;
    @FXML private Label searchLabel;

	// Set up values to be used later
    private final int wordLimit = 40;
    private String currentSearch = "banana";
    private JFXDialog dialog;
    private VideoManager videoManager = VideoManager.getVideoManager();

    // Naughty words to be checked for to protect child - set up
    ArrayList<String> naughtyWords = new ArrayList<>();

	// Search wikipedia for the term specified
    @FXML
    private void searchWiki() {
        // Retrieve the term to search
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;
      
        // Clear the text list of any remaining text from the last search, so no videos can be made with text corresponding to multiple search terms
        textListView.getItems().clear();

        // Perform search
        dialog = new DialogBuilder().loading(stackPane, "Searching for " + searchTerm + "...");
        Task<ArrayList<String>> search = new SearchWiki(searchTerm, textArea, stackPane);
        search.setOnSucceeded(e -> {
            dialog.close();
        });
        Thread thread = new Thread(search);
        thread.start();
        currentSearch = searchTerm;
        autoName();
        checkValidCreate();
    }
    
	// Automatically suggest a name for the video creation upon search of wiki - appears in the input field for video name
    private void autoName() {
    	int i = 0;
    	String currentName = currentSearch + i;
    	System.out.println(videoExists(currentName));
    	while (videoExists(currentName)){
    		i++;
    		currentName = currentSearch + i;
    	}
    	videoNameField.setText(currentName);
    }
    
	// Check if a video already exists
    private boolean videoExists(String name) {
    	for (VideoCreation v: videoManager.getVideos()) {
    		if (v.getName().equals(name)) return true;
    	}
    	return false;
    }

    // Start the video creation process
    @FXML
    private void createVideo() {
        // If no text is selected then raise an error
        if (textListView.getItems().size() == 0) {
            new DialogBuilder().close(stackPane, "Invalid Text", "Please add some text to the list.");
            return;
        }

        // If the user cheekily entered a different word in the search term box (but didn't click search) and tries to make a video, prevent the user from doing so as this new term will be associated with text from a different search term
        if (!currentSearch.equalsIgnoreCase(searchField.getText())) {
            new DialogBuilder().close(stackPane, "Invalid Text", "Complete this search before making a new video. \nOtherwise, change this new word back to the one you previously searched.");
            return;
        }

        // If the user includes a space in the video name, ask them to enter a different video name
        if (videoNameField.getText().contains(" ")) {
            new DialogBuilder().close(stackPane, "Invalid Video Name", "Whoops! You can't have spaces in your video's name- please pick another name.");
            return;
        }

        // If the user searched a banned word, say no results were found and allow to retry
        for (String s : naughtyWords) {
            if (searchField.getText().equals(s)) {
                new DialogBuilder().close(stackPane, "Invalid Search Term", "Whoops! No results were found for this word. Please try another one!");
                return;
            }
        }

        // Start actual creation process
        dialog = new DialogBuilder().loading(stackPane, "Creating Video");
    	createAudio();
    }

    // Convert all text currently displayed in the text list to audio files
    private void createAudio() {
        // Find the original voice name
        String voice = Voice.findVoice(voiceChoiceBox.getSelectionModel().getSelectedItem());
        // Create audio
    	Task createAudiosTask = new CreateAudios(textListView.getItems(), voice);
    	createAudiosTask.setOnSucceeded(e-> {;
    		stitchAudio((ArrayList<String>) createAudiosTask.getValue());
    	});
    	Thread thread = new Thread(createAudiosTask);
    	thread.start();
    }

    // Combine input audio files to make one final audio file
    private void stitchAudio(ArrayList<String> audioFiles) {
    	Task stitchAudioTask = new StitchAudio(audioFiles);
    	stitchAudioTask.setOnSucceeded(e-> combineAudioVideo());
    	Thread thread = new Thread(stitchAudioTask);
    	thread.start();
    }

	// Combine text, audio, and video to create the final video creation
    private void combineAudioVideo() {
        // Retrieve selected number of images
        String videoName = videoNameField.getText();
        Double val = numImages.getValue();
        String finNumImages = Integer.toString(val.intValue());

        // Create the video, and notify the user when it's done
        Task<ArrayList<String>> videoCreation = new CreateVideo(currentSearch, finNumImages, videoName);
        videoCreation.setOnSucceeded(e-> {
            dialog.close();
            ArrayList<String> textContent = new ArrayList<>(textListView.getItems());
            VideoCreation video = new VideoCreation(videoName, currentSearch, (int) numImages.getValue(), textContent);
            videoManager.add(video);    
            new DialogBuilder().close(stackPane, "Video Creation Successful!", videoName + " was created.");
            autoName();
        });
        Thread video = new Thread(videoCreation);
        video.start();
    }

    // Take the highlighted section of text from the search and add it as an item to the list of selected text pieces
    @FXML
    private void add() {
        // Error checking for empty/null selected
        String selectedText = selectedText();
        if (selectedText == null || selectedText.isEmpty()) return;
        if (countWords(selectedText) > wordLimit) {
        	new DialogBuilder().close(stackPane, "Invalid selection", "Please select less than "+ wordLimit +" words");
            return;
        }
        // Add selected as a line in listview
        textListView.getItems().add(selectedText);
    }

    // Remove a piece of text from the list
    @FXML
    private void remove() {
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        textListView.getItems().remove(selected);
    }

    // Swap selected text piece with the piece above it in the list
    @FXML
    private void moveUp() {
        int index= textListView.getSelectionModel().getSelectedIndex();
        String selected = textListView.getSelectionModel().getSelectedItem();
        // Only allow this if there is a piece of text to swap with
        if (index < 1 | selected == null) return;
        textListView.getItems().remove(selected);
        textListView.getItems().add((index-1), selected);
        textListView.getSelectionModel().select(index-1);;
    }

    // Swap selected text piece with the piece below it in the list
    @FXML
    private void moveDown() {
        int index= textListView.getSelectionModel().getSelectedIndex();
        String selected = textListView.getSelectionModel().getSelectedItem();
        // Only allow this if there is a piece of text to swap with
        if (index >= textListView.getItems().size() -1 | selected == null) return;
        textListView.getItems().remove(selected);
        textListView.getItems().add((index+1), selected);
        textListView.getSelectionModel().select(index+1);;
    }
    
	// Go back to the home page
    @FXML
    private void home() {
		new WindowBuilder().switchScene("HomePage", "VarPedia", root.getScene());
    }

    // Quit the application altogether
    @FXML
    private void quit() {
    	searchLabel.getScene().getWindow().hide();
    	videoManager.writeSerializedVideos();
    }

    // Sets up the help buttons and voices dropdown on startup
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	//Voice list
    	updateVoiceList();
    	// Tooltip setup
    	setUpHelp();
    	// Naughty words to be checked for to protect child - set up
        List<String> words = NaughtyWords.getRegularBadWordsList();
        naughtyWords.addAll(words);
    }

    // Refresh the dropdown list of voice options for the video creations
    private void updateVoiceList() {
    	Task<ArrayList<String>> listVoices = new ListVoices();
    	listVoices.setOnSucceeded(e -> {
    		voiceChoiceBox.setItems(FXCollections.observableArrayList(listVoices.getValue()));
    		voiceChoiceBox.getSelectionModel().select(0);
    	});
        Thread thread = new Thread(listVoices);
        thread.start();
    }

    // Add on-hover help messages to the "?" buttons
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

        helpQuitButton.setTooltip(new HoverToolTip("Click this button to exit the application!").getToolTip());
    }

    @FXML
    private void checkValidSearch() {
        // Check search term is valid before allowing the user to press the search button
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchButton.setDisable(true);
        } else {
            searchButton.setDisable(false);
        }
    }

    @FXML
    private void checkValidCreate() {
        // Check creation name is valid before allowing the user to press the button
        String videoName = videoNameField.getText();
        if (videoName == null || videoName.trim().isEmpty() || videoName.contains(" ")) createButton.setDisable(true);
        else createButton.setDisable(false);
        // Disable button if creation of that name exists
        if (videoExists(videoName)) createButton.setDisable(true);
    }

    // return number of words in the input
    private int countWords(String input) {
        if (input == null || input.isEmpty()) {
          return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
      }

      // Format selected text for use
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
