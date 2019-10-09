package app;

import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import processes.*;
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
    private TableView videoTable;
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
    private JFXButton helpVarPedia;
    @FXML
    private Label numVideoLabel;
    @FXML
    private StackPane stackPane;
    
    private VideoManager videoManager = VideoManager.getVideoManager();
    private ArrayList<VideoCreation> toReview = new ArrayList<>();
    private int greenRating = 4;
    private int yellowRating = 2;
    private int redRating = 0;

    @FXML
    private void createVideo() {
    	Stage homeStage = (Stage) helpCreateButton.getScene().getWindow();
    	homeStage.hide();

    	Stage creationStage = new WindowBuilder().pop("NewVideoCreation", "Create a Video!").stage();
    	creationStage.setOnHidden(e -> {
    		updateVideoTable();
    		homeStage.show();
    	});
    }
    
    @FXML
    private void playVideo() {
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	String videoName = videoCreation.getName();
    	if(videoName == null) return;

    	WindowBuilder windowBuilder = new WindowBuilder().pop("VideoPlayer", "Video Player");
    	FXMLLoader loader = windowBuilder.loader();
    	((VideoPlayerController) windowBuilder.loader().getController()).setSource(videoName);
    	windowBuilder.stage().setOnHidden(e -> ((VideoPlayerController) loader.getController()).shutdown());
    }
    
    @FXML
    private void deleteVideo() {
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	String videoName = videoCreation.getName();
    	if(videoName == null) return;
    	System.out.println(videoName);
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Process");
        alert.setHeaderText("Deletion Confirmation");
        alert.setContentText("Would you really like to delete " + videoName + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        
        Task<ArrayList<String>> task = new DeleteVideo(videoName);
        task.setOnSucceeded(event -> updateVideoTable());
        Thread thread = new Thread(task);
        thread.start();
        videoManager.delete(videoCreation);
    }

    @FXML
    private void reviewVideos() {
    	// Determine which videos to review
    	updateVideosToReview();
    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().pop("ReviewPlayer", "Review Videos");
    	ReviewController controller = reviewWindow.loader().getController();
    	
    	controller.setPlaylist(toReview);
    	reviewWindow.stage().setOnHidden(e -> {
    		controller.shutdown();
    		updateVideoTable();
    	});
    }

    @FXML
    private void describeVarPedia() {

    }

    @FXML
    private void describeTableView() {

    }

    @FXML
    private void describeCreate() {

    }

    @FXML
    private void describeDelete() {

    }

    @FXML
    private void describePlay() {

    }

    @FXML
    private void describeReview() {

    }

    @FXML
    private void quit() {
    	VideoManager.getVideoManager().writeSerializedVideos();
    	quitButton.getScene().getWindow().hide();
    }

    private void updateVideoTable() {
    	videoTable.getItems().clear();
    	videoTable.getItems().addAll(videoManager.getVideos());
    }
    
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	initTable();
    	updateVideosToReview();
    	remindReview();
    }
        
    private void initTable() {
    	// Setup coloring of rows based on rating
        videoTable.setRowFactory(tv -> new TableRow<VideoCreation>() {
            @Override
            protected void updateItem(VideoCreation item,boolean empty) {
                super.updateItem(item, empty);
                if (item == null | empty)
                    setStyle("");
                else if (item.getRating() >= greenRating)
                    setStyle("-fx-background-color: #baffba;");
                else if (item.getRating() >= yellowRating)
                    setStyle("-fx-background-color: yellow;");
                else
                    setStyle("-fx-background-color: red;");
            }
        });
        
        // Populate table with columns of parameters of videocreations
        TableColumn<VideoCreation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));        

        TableColumn<VideoCreation, String> searchTermColumn = new TableColumn<>("Search Term");
        searchTermColumn.setMinWidth(80);
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
        
        TableColumn<VideoCreation, String> numImagesColumn = new TableColumn<>("#Images");
        numImagesColumn.setMinWidth(70);
        numImagesColumn.setCellValueFactory(new PropertyValueFactory<>("numImages"));
        
        TableColumn<VideoCreation, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(70);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        
        TableColumn<VideoCreation, String> viewsColumn = new TableColumn<>("Views");
        viewsColumn.setMinWidth(70);
        viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));

        videoTable.getItems().addAll(videoManager.readSerializedVideos());
        videoTable.getColumns().addAll(nameColumn, searchTermColumn, numImagesColumn, ratingColumn, viewsColumn);
    }
    
    private void updateVideosToReview() {
    	toReview.clear();
    	// Add all videos with red ratings
    	for (VideoCreation v: videoManager.getVideos()) {
    		if (v.getRating() < yellowRating) toReview.add(v);
    	}
    	// If no videos with red ratings exist then review yellow ratings
    	if (toReview.size() == 0) {
    		for (VideoCreation v: videoManager.getVideos()) {
        		if (v.getRating() < greenRating) toReview.add(v);
        	}
    	}
    	// If no red or yellow ratings then review everything
    	if (toReview.size() == 0) {
    		toReview = new ArrayList<VideoCreation>(videoTable.getItems());
    	}
    }
    
    private void remindReview() {
    	String body = "";
    	for(VideoCreation v: toReview) {
    		body+= v.getName() + "\n";
    	}
    	new DialogBuilder().closeDialog(stackPane, "Review Reminder", body);
    }

}
