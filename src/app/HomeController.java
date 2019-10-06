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
    private TableView videoTableView;
    @FXML
    private JFXButton helpTableView;
    @FXML
    private JFXButton helpDeleteButton;
    @FXML
    private JFXButton helpCreateButton;
    @FXML
    private JFXButton helpPlayButton;
    @FXML
    private JFXButton helpReviewButton;
    @FXML
    private StackPane stackPane;

    @FXML
    private void createVideo() {
    	try {
    		Stage homeStage = (Stage) helpCreateButton.getScene().getWindow();
    		homeStage.hide();
    		
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("NewVideoCreation.fxml"));
            Scene scene = new Scene(loader.load());
            Stage creationStage = new Stage();
            
            creationStage.setOnCloseRequest(e -> {
            	//updateVideoList();
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
    private void playVideo() {
    	try {
        	String videoString = videoTableView.getSelectionModel().getSelectedItem().toString();
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
    private void deleteVideo() {
    	String videoString = videoTableView.getSelectionModel().getSelectedItem().toString();
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
    private void quit() {
    	quitButton.getScene().getWindow().hide();
    }

    private void updateVideoList() {
    	//Task<Integer> listVideo = new ListVideos(videoList);
    	//listVideo.setOnSucceeded(event -> numVideoLabel.setText("There are currently " + listVideo.getValue() + " videos."));
    	
        //Thread thread = new Thread(listVideo);
        //thread.start();
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
