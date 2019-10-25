package app.controllers;

import com.jfoenix.controls.*;

import app.DialogBuilder;
import app.DraggableWindow;
import app.VideoCreation;
import app.VideoManager;
import app.WindowBuilder;
import app.controllers.helpers.HomeControllerHelper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import processes.*;
import java.util.ArrayList;

/**
 * Controller class for HomePage - is a draggable window
 */
public class HomeController extends DraggableWindow {

    /***************************** FIELD DECLARATIONS ********************************/
	// Root of the scene and main nodes
	@FXML private AnchorPane root;
    @FXML private TableView<VideoCreation> videoTable;
    @FXML private ImageView VARLogo;
	// Help "?" Buttons for this scene
    @FXML private JFXButton helpTableView;
    @FXML private JFXButton helpDeleteButton;
    @FXML private JFXButton helpCreateButton;
    @FXML private JFXButton helpPlayButton;
    @FXML private JFXButton helpReviewButton;
    @FXML private JFXButton helpVarPedia;
    @FXML private JFXButton helpHelp;
    @FXML private JFXButton helpQuitButton;
	// Main menu buttons
    @FXML private JFXButton playButton;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton reviewButton;
	// Bases for notifications (number of videos to review, number of saved videos, and dialogues)
    @FXML private JFXButton reviewNumAlert;
    @FXML private Label numVideoLabel;
    @FXML private StackPane stackPane;
	// Set up the video manager and the list of videos to review
    private VideoManager videoManager = VideoManager.getVideoManager();
    private ArrayList<VideoCreation> toReview = new ArrayList<>();
	// Set up the threshold rating numbers for the colour coding of videos
    private final int greenRating = 4;
    private final int yellowRating = 2;
	// Set up the column widths for the video table - avoiding magic numbers
    private final int nameAndSearchColWidth = 127;
    private final int columnWidthOther = 72;
    private final int favouriteColWidth = 90;
    private HomeControllerHelper helper = new HomeControllerHelper(this);
    /**
     * Open the window that allows the user to create a new video
     */
    @FXML
    private void createVideo() {
        // Hide current window and switch to video creation scene
    	new WindowBuilder().switchScene("VideoCreation", "Create a Video!", root.getScene());
    }

    /**
     * Play the video that the user has selected by opening the video player
     */
    @FXML
    private void playVideo() {
    	
        // Get the video that the user selected
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	// Error checking for null video selected
    	if (videoCreation == null) return;
    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().switchScene("ReviewPlayer", "Review Videos", root.getScene());
    	ReviewController controller = reviewWindow.loader().getController();

    	// Set up the playlist of videos that will be played
    	ArrayList<VideoCreation> playList = new ArrayList<VideoCreation>();
    	playList.add(videoCreation);
    	controller.setPlaylist(playList);
    }

    /**
     * Delete the selected video from the file system and table view
     */
    @FXML
    private void deleteVideo() {
    	
        // Get the video that the user selected
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();

	   // Use dialogue to make user confirm deletion of video
    	DialogBuilder confirmDelete = new DialogBuilder();
        JFXButton confirm = confirmDelete.confirm(stackPane, "Deletion Confirmation", "Would you really like to delete " + videoCreation.getName() + "?");

        // Delete the video if confirm was pressed
        confirm.setOnAction( e-> {
            Task<ArrayList<String>> task = new DeleteVideo(videoCreation.getName());
            task.setOnSucceeded(event -> {
            	videoManager.delete(videoCreation);
            	// Update the video table and confirm that the video was deleted
                helper.updateVideoTable(videoTable, videoManager, numVideoLabel, playButton, deleteButton,  reviewButton);
            	helper.updateVideosToReview(toReview, videoManager, reviewNumAlert, videoTable, greenRating, yellowRating);
                helper.disableVideoButtons(playButton, deleteButton, reviewButton, videoTable);
            	confirmDelete.dialog().close();
            	new DialogBuilder().close(stackPane, "Deletion Success", videoCreation.getName() + " has been deleted!");
            });
            Thread thread = new Thread(task);
            thread.start();
        });
    }

    /**
     * Set up and show the video player for the user to review videos according to priority (priority is decided by rating)
     */
    @FXML
    public void reviewVideos() {
    	// Update videos to review
    	helper.updateVideosToReview(toReview, videoManager, reviewNumAlert, videoTable, greenRating, yellowRating);

    	// Don't launch if there are zero videos
    	if(toReview.isEmpty()) {
    		new DialogBuilder().close(stackPane, "Review Videos", "There are currently no videos to review.");
    		return;
    	}

    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().switchScene("ReviewPlayer", "Review Videos", root.getScene());
    	ReviewController controller = reviewWindow.loader().getController();
    	controller.setPlaylist(toReview);
    }

    /**
     * Quit the application
     */
    @FXML
    private void quit() {
	    // Write to video files
    	videoManager.writeSerializedVideos();
	    // Quit the application
    	helpQuitButton.getScene().getWindow().hide();
    }

    /**
     * Check if the user has selected a video from the table view, and enable or disable the play and delete buttons accordingly.
     */
    @FXML
    private void checkValidVideo() {
        if (videoTable.getSelectionModel().getSelectedItem() == null) {
            helper.disableVideoButtons(playButton, deleteButton, reviewButton, videoTable);
        } else {
            helper.enableVideoButtons(playButton, deleteButton);
        }
    }

    /**
     * Is run first on startup to set up the tableView, videos, and help buttons
     */
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
        helper.initTable(videoTable, videoManager, numVideoLabel, playButton, deleteButton,  reviewButton, 
        		favouriteColWidth, greenRating, yellowRating, nameAndSearchColWidth, columnWidthOther);
    	helper.updateVideosToReview(toReview, videoManager, reviewNumAlert, videoTable, greenRating, yellowRating);
        helper.setUpHelp(helpTableView, helpCreateButton, helpDeleteButton, helpHelp, helpPlayButton, helpQuitButton, helpReviewButton, helpVarPedia);
        helper.updateVideoTable(videoTable, videoManager, numVideoLabel, playButton, deleteButton,  reviewButton);
        helper.checkVideosExist(playButton, deleteButton, reviewButton, videoTable);
        helper.disableVideoButtons(playButton, deleteButton, reviewButton, videoTable);
    }
    
    public void remindReview() {
    	helper.remindReview(toReview, stackPane);
    }

}
