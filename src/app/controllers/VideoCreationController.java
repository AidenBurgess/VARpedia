package app.controllers;

import app.*;
import app.controllers.helpers.VideoCreationControllerHelper;
import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import processes.*;
import java.util.ArrayList;

/**
 * Controller class for the video creation scene - is a draggable window
 */
public class VideoCreationController extends DraggableWindow {

    /***************************** FIELD DECLARATIONS ********************************/
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
    @FXML private JFXButton moveUpButton;
    @FXML private JFXButton moveDownButton;
    @FXML private JFXButton addButton;
    @FXML private JFXButton removeButton;
	
	// Dialogue and labels
    @FXML private StackPane stackPane;
    @FXML private Label searchLabel;

	// Set up values to be used later
    private final int wordLimit = 40;
    private String currentSearch = "banana";
    private JFXDialog dialog;
    private VideoManager videoManager = VideoManager.getVideoManager();

    // Set up a helper object that has access to the helper methods
    private VideoCreationControllerHelper helper = new VideoCreationControllerHelper();


    /***************************** FXML METHODS ********************************/

    /**
     * Search wikipedia for the term specified
     */
    @FXML
    private void searchWiki() {
        // Retrieve the term to search
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        for (String s : NaughtyWords.getRegularBadWordsList()) {
            if (searchTerm.equalsIgnoreCase(s)) {
                new DialogBuilder().close(stackPane, "Invalid Search Term", "Whoops! No results were found for this word. Please try another one!");
                return;
            }
        }
      
        // Clear the text list of any remaining text from the last search, so no videos can be made with text corresponding to multiple search terms
        textListView.getItems().clear();

        // Perform search
        dialog = new DialogBuilder().loading(stackPane, "Searching for " + searchTerm + "...");
        Task<ArrayList<String>> search = new SearchWiki(searchTerm, textArea, stackPane);
        search.setOnSucceeded(e -> {
            dialog.close();
            helper.updateMoveButtons(textListView, moveUpButton, moveDownButton);
        });
        Thread thread = new Thread(search);
        thread.start();
        currentSearch = searchTerm;
        videoNameField.setText(helper.autoName(currentSearch));
        checkValidCreate();
    }

    /**
     * Start the video creation process
     */
    @FXML
    private void createVideo() {
        // Check for errors in user input
        helper.checkCanMakeVideo(stackPane, textListView, videoNameField);
        helper.checkSearchFieldEntry(stackPane, searchField, currentSearch);

        // Start creation process visually for the user
        dialog = new DialogBuilder().loading(stackPane, "Creating Video");
        createButton.setDisable(true);

        // Start the creation process behind the scenes
        // Find the original voice name
        String voice = Voice.findVoice(voiceChoiceBox.getSelectionModel().getSelectedItem());
        // Create audio files based on the text in the list of text pieces
        Task createAudiosTask = new CreateAudios(textListView.getItems(), voice);
        // After all the audio files have been made, combine them into one audio file
        createAudiosTask.setOnSucceeded(e-> {
            Task stitchAudioTask = new StitchAudio((ArrayList<String>) createAudiosTask.getValue());
            // When the audio file has been made, create the video by combining the audio file with a video file
            stitchAudioTask.setOnSucceeded(f-> {
                combineAudioVideo();
            });
            Thread thread = new Thread(stitchAudioTask);
            thread.start();
        });
        Thread thread = new Thread(createAudiosTask);
        thread.start();
    }

    /**
     * Take the highlighted section of text from the search and add it as an item to the list of selected text pieces
     */
    @FXML
    private void add() {
        // Error checking for empty/null selected
        String selectedText = helper.selectedText(textArea.getSelectedText().split("\t"));
        if (selectedText == null || selectedText.isEmpty()) return;
        if (helper.countWords(selectedText) > wordLimit) {
        	new DialogBuilder().close(stackPane, "Invalid selection", "Please select less than "+ wordLimit +" words");
            return;
        }
        // Add selected as a line in listview
        textListView.getItems().add(selectedText);
        helper.updateMoveButtons(textListView, moveUpButton, moveDownButton);
    }

    /**
     * Remove a piece of text from the list
     */
    @FXML
    private void remove() {
        String selected = textListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        textListView.getItems().remove(selected);
        helper.updateMoveButtons(textListView, moveUpButton, moveDownButton);
    }

    /**
     * Swap selected text piece with the piece above it in the list
     */
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

    /**
     * Swap selected text piece with the piece below it in the list
     */
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

    /**
     * Go back to the home page
     */
    @FXML
    private void home() {
		new WindowBuilder().switchScene("HomePage", "VarPedia", root.getScene());
    }

    /**
     * Quit the application altogether
     */
    @FXML
    private void quit() {
    	searchLabel.getScene().getWindow().hide();
    	videoManager.writeSerializedVideos();
    }

    /**
     * Sets up the help buttons and voices dropdown on startup
     */
    @FXML
    private void initialize() {
    	// Make stackPane transparent
    	stackPane.setPickOnBounds(false);
    	// Add event handler for selection in textArea
    	textArea.selectionProperty().addListener(e-> {
    	    String in = helper.selectedText(textArea.getSelectedText().split("\t"));
    		addButton.setDisable(helper.countWords(in) == 0 || helper.countWords(in) > wordLimit);
    	});
    	// Add event handler for selection of listview
    	textListView.getSelectionModel().selectedItemProperty().addListener(e-> {
    		removeButton.setDisable(textListView.getSelectionModel().getSelectedItem() == null);
    	});
    	// Allow text wrapping in ListView
    	textListView.setCellFactory(param -> new ListCell<String>(){
    		@Override
    		protected void updateItem(String item, boolean empty) {
    			super.updateItem(item, empty);
    			if (empty || item.isEmpty()) {
    				setText("");
    				setGraphic(null);
    				return;
    			}
    			setMinWidth(param.getWidth()-2);
    			setMaxWidth(param.getWidth()-2);
    			setPrefWidth(param.getWidth()-2);
    			setWrapText(true);
    			setText(item.toString());
    		}
    	});
    	//Voice list
    	helper.updateVoiceList(voiceChoiceBox);
    	// Tooltip setup
    	setUpHelp();
    }

    /**
     * Check if a search term is valid (it is valid if the search term field is not empty)
     */
    @FXML
    private void checkValidSearch() {
        // Check search term is valid before allowing the user to press the search button
        String searchTerm = searchField.getText();
        boolean isInvalid = searchTerm == null || searchTerm.trim().isEmpty();
        searchButton.setDisable(isInvalid);
    }

    /**
     * Check if the video creation name is valid or not
     */
    @FXML
    private void checkValidCreate() {
        // Check creation name is valid before allowing the user to press the button
        String videoName = videoNameField.getText();
        boolean isInvalid = videoName == null || videoName.trim().isEmpty() || videoName.contains(" ") || helper.videoExists(videoName);
        createButton.setDisable(isInvalid);
    }

    /**
     * Add on-hover help messages to the "?" buttons
     */
    private void setUpHelp() {
        helpSearchResultsButton.setTooltip(new HoverToolTip("Your search results will appear here. \nYou can click and drag to select text, add text to the results by typing it in, or delete text you don't want to see!").getToolTip());
        helpAddRemoveButton.setTooltip(new HoverToolTip("After selecting some text from the left, press the \">\" button to add it to the list on the right. \nOr, after selecting one or more of the pieces of text from the right, click the \"<\" button to delete them from the list on the right!").getToolTip());
        helpShuffleButton.setTooltip(new HoverToolTip("After selecting a piece of text from the right, click the \"^\" or \"v\" buttons to move that piec up or down in the list!").getToolTip());
        helpBackButton.setTooltip(new HoverToolTip("Click this button to go back to the main menu!").getToolTip());
        helpSearchButton.setTooltip(new HoverToolTip("Type in the word you want to search the meaning for to the left of this button, then click this button to search it!").getToolTip());
        helpCreateButton.setTooltip(new HoverToolTip("After filling in the video name, number of images, and the voice you want to use in the areas to the left, click this button to make your video!\nYou won't be able to click this if the video name you put in has spaces in it, or if there is already a video with that name!").getToolTip());
        helpVoicesButton.setTooltip(new HoverToolTip("In this list are different voices you can choose to speak your text in your video! \nClick it to see the options, then click one of the options shown to choose it!").getToolTip());
        helpVideoNameButton.setTooltip(new HoverToolTip("Type in what you want to name your final video here! \nMake sure it doesn't include any spaces!").getToolTip());
        helpNumImagesButton.setTooltip(new HoverToolTip("Click and drag the dot along the line to choose how many picture you want to have in your video, from 1 to 10!").getToolTip());
        helpTextListButton.setTooltip(new HoverToolTip("This is where each bit of text is shown in a list! \nSelect one piece of text by clicking on it!").getToolTip());
        helpQuitButton.setTooltip(new HoverToolTip("Click this button to exit the application!").getToolTip());
    }

    /**
     * Combine text, audio, and video to create the final video creation
     */
    public void combineAudioVideo() {
        // Retrieve selected number of images
        String finNumImages = Integer.toString((int) numImages.getValue());
        String videoName = videoNameField.getText();

        // Create the video, and notify the user when it's done
        Task<ArrayList<String>> videoCreation = new CreateVideo(currentSearch, finNumImages, videoName);
        videoCreation.setOnSucceeded(e-> {
            dialog.close();
            ArrayList<String> textContent = new ArrayList<>(textListView.getItems());
            VideoCreation video = new VideoCreation(videoName, currentSearch, (int) numImages.getValue(), textContent);
            videoManager.add(video);
            new DialogBuilder().close(stackPane, "Video Creation Successful!", videoName + " was created.");
            createButton.setDisable(false);
            helper.autoName(currentSearch);
        });
        Thread video = new Thread(videoCreation);
        video.start();
    }

}
