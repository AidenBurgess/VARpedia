package app;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import processes.*;

public class VideoPageController {
	
	@FXML
	private JFXListView<String> availableAudio;
	@FXML
	private JFXListView<String> chosenAudio;
	@FXML
	private JFXListView<String> videoList;
	@FXML
	private StackPane stackPane;
	@FXML
	private JFXTextField customNameField;
	@FXML
	private JFXTextField numImagesField;
	@FXML
	private JFXTextField searchTermField;

	private ObservableList<String> chosenAudioItems;
	

    @FXML
    private void initialize() {
    	updateVideoList();
        Task<Integer> listAudio = new ListAudio(availableAudio);
        Thread thread = new Thread(listAudio);
        thread.start();
        
        chosenAudioItems = chosenAudio.getItems();
    }

    @FXML
    private void quit() {
    	videoList.getScene().getWindow().hide();
    }

    @FXML
    private void add() {
    	String selected = availableAudio.getSelectionModel().getSelectedItem();
    	if (selected == null) return;
    	chosenAudioItems.add(selected);
    	availableAudio.getItems().remove(selected);
    }
    
    @FXML
    private void remove() {
    	String selected = chosenAudio.getSelectionModel().getSelectedItem();
    	if (selected == null) return;
    	availableAudio.getItems().add(selected);
    	chosenAudioItems.remove(selected);
    }
    
    @FXML
    private void moveUp() {
    	int index= chosenAudio.getSelectionModel().getSelectedIndex();
    	String selected = chosenAudio.getSelectionModel().getSelectedItem();
    	if (index < 1 | selected == null) return;
    	// Now swap items
    	String tempString = chosenAudio.getItems().get(index-1);
    	chosenAudioItems.set(index, tempString);
    	chosenAudioItems.set(index-1, selected);
    	chosenAudio.getSelectionModel().select(index-1);;
    }
    
    @FXML
    private void moveDown() {
    	int index= chosenAudio.getSelectionModel().getSelectedIndex();
    	String selected = chosenAudio.getSelectionModel().getSelectedItem();
    	if (index >= chosenAudio.getItems().size() -1 | selected == null) return;
    	// Now swap items
    	String tempString = chosenAudio.getItems().get(index+1);
    	chosenAudioItems.set(index, tempString);
    	chosenAudioItems.set(index+1, selected);
    	chosenAudio.getSelectionModel().select(index+1);;
    }
    
    @FXML
    private void playVideo() {
    	try {
        	String videoString = videoList.getSelectionModel().getSelectedItem();
        	if(videoString == null || videoString == "") return;

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
    private void createVideo() {
    	// If no audio is selected then raise an error
    	if (chosenAudioItems.size() == 0) {
    		alertCreator("Selection Process", "Invalid Selection", "Please select some audio.");
    		return;
    	}
    	
    	// Error checking for empty/null selected
    	String customName = customNameField.getText();
    	String searchTerm = searchTermField.getText();
    	if (customName == null || customName.isEmpty()) return;
    	if(searchTerm == null || searchTerm == "") return;
    	
    	// Error checking for already existing audio
    	if (videoList.getItems().contains(customName)) {
    		alertCreator("Creation Process", "Creating video error", customName + " already exists, please choose another name.");
    	    return;
    	}
    	
    	// Check if number of images is a number, is not empty, is not null, and is within 0 and 10.
    	String numImages = numImagesField.getText();
        if (!isNumeric(numImages) || numImages.isEmpty() || numImages == null || Integer.parseInt(numImages) > 10 || Integer.parseInt(numImages) < 1) {
            alertCreator("Creation Process", "Number of images", "Please enter a number of images between 1 and 10 (inclusive).");
            return;
        }
        
        stitchAudio();
        combineAudioVideo();
    }

    @FXML
    private void playChosenAudio() {
    	String selected = chosenAudio.getSelectionModel().getSelectedItem();
    	playAudio(selected);
    }

    @FXML
    private void playAvailableAudio() {
    	String selected = availableAudio.getSelectionModel().getSelectedItem();
    	playAudio(selected);
    }

    private void playAudio(String selected) {
    	if (selected == null || selected == "") return;
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
    
    private void stitchAudio() {
    	Task<ArrayList<String>> stitchAudio = new StitchAudio(chosenAudioItems);
    	Thread thread = new Thread(stitchAudio);
    	thread.run();
    }
    
    private void combineAudioVideo() {
        String searchTerm = searchTermField.getText();
        String customName = customNameField.getText();
        String videoName = customName;
        String numImages = numImagesField.getText();
        
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text("Creating video"));
        dialogContent.setBody(new JFXSpinner());
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
        dialog.show();
        
        Task<Boolean> task = new VideoExists(videoName);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (EventHandler<WorkerStateEvent>) t -> {
        	Task<ArrayList<String>> videoCreation = new CreateVideo(searchTerm, numImages, videoName);
        	videoCreation.setOnSucceeded(e -> {
        		updateVideoList();
        		dialog.close();
        	});

        	Thread video = new Thread(videoCreation);
        	video.start();
        });
            
        Thread thread = new Thread(task);
        thread.start();
    }
    
    // Refresh video list
    private void updateVideoList() {
    	Task<Integer> listVideo = new ListVideos(videoList);    	
        Thread thread = new Thread(listVideo);
        thread.start();
    }

    // Helper method for error checking
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
