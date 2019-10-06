package app;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import processes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class HomeController {

	@FXML
    private JFXListView<String> videoList;
    @FXML
    private JFXListView<String> audioList;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton createAudioButton;
    @FXML
    private JFXTextField searchField;
    @FXML
    private Label searchLabel;
    @FXML
    private Label numVideoLabel;
    @FXML
    private Label numAudioLabel;
    @FXML
    private JFXTextArea textArea;
    @FXML
    private JFXTextField customNameField;
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXComboBox<String> voiceChoiceBox;
    



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
    private void createAudio() {
    	
    	// Error checking for empty/null selected
    	String selectedText = selectedText();
    	String customName = customNameField.getText();
    	if (selectedText == null || selectedText.isEmpty()) return;
    	if (customName == null || customName.isEmpty()) return;

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
    	
    	// Error checking for already existing audio
    	if (audioList.getItems().contains(customName)) {
    	    JFXDialogLayout dialogContent = new JFXDialogLayout();
    	    dialogContent.setHeading(new Text("Creating audio error"));
    	    dialogContent.setBody(new Text(customName + " already exists, please choose another name."));
    	    JFXButton close = new JFXButton("Close");
    	    close.getStyleClass().add("JFXButton");
    	    dialogContent.setActions(close);
    	    JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
    	    close.setOnAction( e -> dialog.close());
    	    dialog.show();
    	    return;
    	}
    	
    	// Disable audio button to prevent spamming of button
    	createAudioButton.setDisable(true);
    	
    	Task<ArrayList<String>> create = new CreateAudio(customName, selectedText, voiceChoiceBox.getSelectionModel().getSelectedItem());
    	JFXDialog dialog = loadingDialog("Creating audio...");
    	create.setOnSucceeded(e -> {
    		dialog.close();
    		createAudioButton.setDisable(false);
    	});
    	
    	// Start the creation process for audio
    	Thread thread = new Thread(create);
    	thread.start();
    }

    @FXML
    private void createVideo() {
    	try {
    		Stage homeStage = (Stage) searchField.getScene().getWindow();
    		homeStage.hide();
    		
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("VideoPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage creationStage = new Stage();
            
            creationStage.setOnCloseRequest(e -> {
            	updateVideoList();
            	homeStage.show();
            });
            creationStage.setTitle("Create a Video!");
            creationStage.setResizable(false);
            creationStage.setScene(scene);
            creationStage.show();            
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    @FXML
    private void playAudio() {
    	String selected = audioList.getSelectionModel().getSelectedItem();
    	if (selected == null || selected.isEmpty()) return;
    	Thread thread = new Thread(()->  {
    		ProcessBuilder pb = new ProcessBuilder().command("aplay", "audio/" + selected + ".wav");
            try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	});
    	thread.start();
    }
    
    @FXML
    private void playVideo() {
    	try {
        	String videoString = videoList.getSelectionModel().getSelectedItem();
        	if(videoString == null || videoString.isEmpty()) return;
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("VideoPlayer.fxml"));
            Parent layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = new Stage();
            
            ((VideoPlayerController) loader.getController()).setSource(videoString);
            stage.setOnCloseRequest(e -> ((VideoPlayerController) loader.getController()).shutdown());
            stage.setTitle("Video Player");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    @FXML
    private void deleteAudio() {
    	String audioString = audioList.getSelectionModel().getSelectedItem();
    	if (audioString == null || audioString.isEmpty()) return;
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Process");
        alert.setHeaderText("Deletion Confirmation");
        alert.setContentText("Would you really like to delete " + audioString + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        
        Task<ArrayList<String>> task = new DeleteAudio(audioString);
        Thread thread = new Thread(task);
        thread.start();
    }
    
    @FXML
    private void deleteVideo() {
    	String videoString = videoList.getSelectionModel().getSelectedItem();
    	if(videoString == null || videoString.isEmpty()) return;
    	System.out.println(videoString);
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Process");
        alert.setHeaderText("Deletion Confirmation");
        alert.setContentText("Would you really like to delete " + videoString + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        
        Task<ArrayList<String>> task = new DeleteVideo(videoString);
        task.setOnSucceeded(event -> updateVideoList());
        Thread thread = new Thread(task);
        thread.start();
    }
    
    @FXML
    private void previewAudio() {
    	String selectedText = selectedText();
    	if (selectedText == null || selectedText.isEmpty()) return;
    	
    	String voice = voiceChoiceBox.getSelectionModel().getSelectedItem();
    	if (voice == null || voice == "") return;
    	
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
    	Thread thread = new Thread(new PreviewAudio(selectedText, voice));
        thread.start();
    }

    @FXML
    private void quit() {
    	numVideoLabel.getScene().getWindow().hide();
    }

    private void updateVideoList() {
    	Task<Integer> listVideo = new ListVideos(videoList);
    	listVideo.setOnSucceeded(event -> numVideoLabel.setText("There are currently " + listVideo.getValue() + " videos."));
    	
        Thread thread = new Thread(listVideo);
        thread.start();
    }
    
    @FXML
    private void initialize() {
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
