package app;

import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import processes.*;

import java.io.IOException;
import java.util.ArrayList;
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
    private JFXComboBox voiceChoiceBox;
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
        searchField.setText("");
    }

    @FXML
    private void createVideo() {

        // If no text is selected then raise an error
        if (textListView.getSelectionModel().getSelectedItems().size() == 0) {
            alertCreator("Selection Process", "Invalid Selection", "Please select some text.");
            return;
        }

        // Error checking for empty/null selected
        String customName = videoNameField.getText();
        String searchTerm = searchField.getText();
        if (customName == null || customName.isEmpty()) return;
        if (searchTerm == null || searchTerm == "") return;

        //Change to combine text and then create one audio file from that. THEN call combineAudioVideo()
       // String text = stitchText(selectedText);
       // createAudio(text);
        combineAudioVideo();
    }

    private String stitchText(ArrayList<String> selected) {
        String output = "";
        for (String s : selected) {
            output += " ";
            output += s;
        }
        return output;
    }

    private void createAudio(String text) {
       // Task<ArrayList<String>> create = new CreateAudio(videoNameField, text, voiceChoiceBox.getSelectionModel().getSelectedItem());

        //Start the creation process for audio
        //Thread thread = new Thread(create);
        //thread.start();
    }

    private void combineAudioVideo() {
        String searchTerm = searchField.getText();
        String videoName = videoNameField.getText();
        Double val = numImages.getValue();
        String finNumImages = Integer.toString(val.intValue());


        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text("Creating video"));
        dialogContent.setBody(new ImageView("loading.gif"));
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
        dialog.show();

        Task<Boolean> task = new VideoExists(videoName);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (EventHandler<WorkerStateEvent>) t -> {
            Task<ArrayList<String>> videoCreation = new CreateVideo(searchTerm, finNumImages, videoName);
            videoCreation.setOnSucceeded(e -> {
                    dialog.close();
            });

            Thread video = new Thread(videoCreation);
            video.start();
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void add() {
        // Error checking for empty/null selected
        String selectedText = selectedText();
        if (selectedText == null || selectedText.isEmpty()) return;

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
        textListView.getSelectionModel().select(index-1);;
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
    }

    @FXML
    private void describeSearchResults() {
    }

    @FXML
    private void describeAddRemoveText() {
    }

    @FXML
    private void describeShuffleText() {
    }

    @FXML
    private void describeBackButton() {
    }

    @FXML
    private void describeSearchFunction() {
    }

    @FXML
    private void describeChooseVoice() {
    }

    @FXML
    private void describeVideoNameField() {
    }

    @FXML
    private void describeImagesSlider() {
    }

    @FXML
    private void describeCreateButton() {
    }

    @FXML
    private void describeTextList() {
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
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
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
