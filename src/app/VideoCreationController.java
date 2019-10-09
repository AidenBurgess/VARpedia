package app;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import processes.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    	System.out.println(textListView.getItems());
    	createAudio();
    }

    private void createAudio() {
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text("Creating video"));
        dialogContent.setBody(new JFXSpinner());
        dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
        dialog.show();

    	System.out.println("Create audio called");
    	Task createAudiosTask = new CreateAudios(textListView.getItems(), voiceChoiceBox.getSelectionModel().getSelectedItem());
    	createAudiosTask.setOnSucceeded(e-> {;
    		System.out.println("Finished making audio");
    		stitchAudio((ArrayList<String>) createAudiosTask.getValue());
    	});
    	Thread thread = new Thread(createAudiosTask);
    	thread.start();
    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    private void stitchAudio(ArrayList<String> audioFiles) {
    	System.out.println("Stitch audio called");
    	Task stitchAudioTask = new StitchAudio(audioFiles);
    	stitchAudioTask.setOnSucceeded(e-> combineAudioVideo());
    	Thread thread = new Thread(stitchAudioTask);
    	thread.start();
    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    private void combineAudioVideo() {
        String videoName = videoNameField.getText();
        Double val = numImages.getValue();
        String finNumImages = Integer.toString(val.intValue());

        Task<Boolean> task = new VideoExists(videoName);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (EventHandler<WorkerStateEvent>) t -> {
            Task<ArrayList<String>> videoCreation = new CreateVideo(currentSearch, finNumImages, videoName);
            Thread video = new Thread(videoCreation);
            video.start();
            dialog.close();
            VideoManager.getVideoManager().add(new VideoCreation(videoName, currentSearch, (int) numImages.getValue()));
        });

        Thread thread = new Thread(task);
        thread.start();
        try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void add() {
        // Error checking for empty/null selected
        String selectedText = selectedText();
        if (selectedText == null || selectedText.isEmpty()) return;
        System.out.println(selectedText);

        if (countWords(selectedText) > 40) {
            JFXDialogLayout dialogContent = new JFXDialogLayout();
            dialogContent.setHeading(new Text("Invalid Selection"));
            dialogContent.setBody(new Text("Please select less than 40 words."));
            JFXButton close = new JFXButton("Close");
            close.getStyleClass().add("JFXButton");
            dialogContent.setActions(close);
            JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
            close.setOnAction( e -> dialog.close());
            dialog.show();
            return;
        }
        //fix so new when adding text it comes up in its own item in the list
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
        helpSearchResultsButton.setTooltip(new Tooltip("Your search results will appear here. \nYou can click and drag to select text, add text to the results by typing it in, or delete text you don't want to see!"));
        helpAddRemoveButton.setTooltip(new Tooltip(""));
        helpShuffleButton.setTooltip(new Tooltip(""));
        helpBackButton.setTooltip(new Tooltip("Click this button to go back to the main menu!"));
        helpSearchButton.setTooltip(new Tooltip(""));
        helpSearchResultsButton.setTooltip(new Tooltip(""));
        helpVoicesButton.setTooltip(new Tooltip("In this list are different voices you can choose to speak your text in your video"));
        helpVideoNameButton.setTooltip(new Tooltip(""));
        helpNumImagesButton.setTooltip(new Tooltip(""));
        helpTextListButton.setTooltip(new Tooltip(""));

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

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) if (!Character.isDigit(c)) return false;
        return true;
    }

    private void alertCreator(String title, String header, String content) {
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(header == null ? title : title + "\n" + header));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.getStyleClass().add("JFXButton");
        dialogContent.setActions(close);
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.CENTER);
        close.setOnAction(e-> dialog.close());
        dialog.show();
    }

}
