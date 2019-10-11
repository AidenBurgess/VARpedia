package app;

import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import processes.*;
import java.util.ArrayList;
import java.util.Optional;

public class HomeController {
	
	@FXML
	private AnchorPane root;
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
    private JFXButton helpHelp;
    @FXML
    private JFXButton helpQuitButton;
    @FXML
    private Label numVideoLabel;
    @FXML
    private StackPane stackPane;
    
    private VideoManager videoManager = VideoManager.getVideoManager();
    private ArrayList<VideoCreation> toReview = new ArrayList<>();
    private int greenRating = 4;
    private int yellowRating = 2;
    private int redRating = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void createVideo() {
        // Hide current window
    	Stage homeStage = (Stage) helpCreateButton.getScene().getWindow();
    	homeStage.hide();

    	// Bring up the create video scene
    	Stage creationStage = new WindowBuilder().pop("NewVideoCreation", "Create a Video!").stage();
    	creationStage.setOnHidden(e -> {
    		updateVideoTable();
    		homeStage.show();
    	});
    }
    
    @FXML
    private void playVideo() {
        // Get the video that the user selected
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	if(videoCreation == null) return;

    	// Brings up the video player with the selected video
    	WindowBuilder windowBuilder = new WindowBuilder().pop("VideoPlayer", "Video Player");
    	FXMLLoader loader = windowBuilder.loader();
    	((VideoPlayerController) windowBuilder.loader().getController()).setSource(videoCreation.getName());
    	windowBuilder.stage().setOnHidden(e -> ((VideoPlayerController) loader.getController()).shutdown());
    }
    
    @FXML
    private void deleteVideo() {
        // Get the video that the user selected
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	if(videoCreation == null) return;

    	// Confirm user wants to delete the video
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Process");
        alert.setHeaderText("Deletion Confirmation");
        alert.setContentText("Would you really like to delete " + videoCreation.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        
        Task<ArrayList<String>> task = new DeleteVideo(videoCreation.getName());
        task.setOnSucceeded(event -> updateVideoTable());
        Thread thread = new Thread(task);
        thread.start();
        videoManager.delete(videoCreation);
    }

    @FXML
    private void reviewVideos() {
    	// Update videos to review
    	updateVideosToReview();
    	// Don't launch if there are zero videos
    	if(toReview.isEmpty()) {
    		new DialogBuilder().closeDialog(stackPane, "Review Videos", "There are currently no videos to review.");
    		return;
    	}
    	// Close current stage
    	Stage homeStage = (Stage) helpCreateButton.getScene().getWindow();
    	homeStage.hide();
    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().noTop("ReviewPlayer", "Review Videos");
    	ReviewController controller = reviewWindow.loader().getController();
    	controller.setPlaylist(toReview);
    }

    @FXML
    private void quit() {
    	VideoManager.getVideoManager().writeSerializedVideos();
    	helpQuitButton.getScene().getWindow().hide();
    }

    // Refresh the video table with any updates
    private void updateVideoTable() {
    	videoTable.getItems().clear();
    	videoTable.getItems().addAll(videoManager.getVideos());

    	int num = videoTable.getItems().size();
    	numVideoLabel.setText("There are currently " + num + " videos!");
    }

    // Is run first on startup to set up the tableView and help buttons
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	initTable();
    	updateVideosToReview();
        setUpHelp();
        updateVideoTable();
    }
        
    private void initTable() {
    	// Setup coloring of rows based on rating
        videoTable.setRowFactory(tv -> new TableRow<VideoCreation>() {
            @Override
            protected void updateItem(VideoCreation item,boolean empty) {
                super.updateItem(item, empty);
                if (item == null | empty)
                    setStyle("");
                else if (item.getRating() >= greenRating) {
                    getStyleClass().clear();
                    getStyleClass().add("green-row");
                }
                else if (item.getRating() >= yellowRating) {
                    getStyleClass().clear();
                    getStyleClass().add("yellow-row");
                }
                else {
                    getStyleClass().clear();
                    getStyleClass().add("red-row");
                }
            }
        });
      
        videoTable.setStyle("-fx-selection-bar: blue; -fx-selection-bar-non-focused: purple;");
        // Populate table with columns of parameters of videocreations (Name, search term, #images, rating, views)
        TableColumn<VideoCreation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));        

        TableColumn<VideoCreation, String> searchTermColumn = new TableColumn<>("Search Term");
        searchTermColumn.setMinWidth(150);
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
        
        TableColumn<VideoCreation, String> numImagesColumn = new TableColumn<>("#Images");
        numImagesColumn.setMinWidth(89);
        numImagesColumn.setCellValueFactory(new PropertyValueFactory<>("numImages"));
        
        TableColumn<VideoCreation, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(89);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<VideoCreation, String> viewsColumn = new TableColumn<>("Views");
        viewsColumn.setMinWidth(89);
        viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));
        
        videoTable.getItems().addAll(videoManager.readSerializedVideos());
        videoTable.getColumns().addAll(nameColumn, searchTermColumn, numImagesColumn, ratingColumn, viewsColumn);
    	numVideoLabel.setText("There are " + videoTable.getItems().size() + " videos");
    }

    // Add on-hover help messages to the "?" buttons
    private void setUpHelp() {
        helpTableView.setTooltip(new HoverToolTip("All of your video creations are listed here! Click on a row to select that video. The columns show you: \nthe name of each video; \nthe word you searched to create the video; \nthe number of images the video has in it; \nthe rating out of 5 you gave each video; \nthe number of times you have watched each video.").getToolTip());

        helpCreateButton.setTooltip(new HoverToolTip("Click this button to start creating a new video! (Opens a new window)").getToolTip());

        helpDeleteButton.setTooltip(new HoverToolTip("After selecting a video from the table by clicking on one of the rows, click this button to delete it!").getToolTip());

        helpHelp.setTooltip(new HoverToolTip("This is what the hover text will look like!").getToolTip());

        helpPlayButton.setTooltip(new HoverToolTip("After selecting a video from the table by clicking on one of the rows, click this button to play it!").getToolTip());

        helpQuitButton.setTooltip(new HoverToolTip("Click this button to exit the application!").getToolTip());

        helpReviewButton.setTooltip(new HoverToolTip("Click this button to watch and review multiple videos in a playlist shown to you, and rate your understanding of each of them!").getToolTip());

        helpVarPedia.setTooltip(new HoverToolTip("Welcome! This is VARpedia. \nThis application is made for you to learn new words by letting you create videos about them. \nThese videos will show you images of the word you choose, have a voice saying text about the word to you, and show you the word written down. \nThese videos are saved so you can go back to review words you are unsure about, and rate the different videos you have made based on your understanding of it!").getToolTip());
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

    // Shown on startup, present a list of suggested videos for the user to review
    public void remindReview() {
    	String body = "";
    	for(VideoCreation v: toReview) {
    		body+= v.getName() + "\n";
    	}
    	new DialogBuilder().closeDialog(stackPane, "Review Reminder", body);
    }

    // Allows the user to move window around on the screen
    public void makeStageDrageable() {
    	Stage stage = (Stage) root.getScene().getWindow();
        root.setOnMousePressed(event-> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event-> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
                stage.setOpacity(0.8f);
        });
        root.setOnDragDone((e) -> {
            stage.setOpacity(1.0f);
        });
        root.setOnMouseReleased((e) -> {
            stage.setOpacity(1.0f);
        });
    }

}
