package app.controllers;

import com.jfoenix.controls.*;

import app.DialogBuilder;
import app.DraggableWindow;
import app.HoverToolTip;
import app.VideoCreation;
import app.VideoManager;
import app.WindowBuilder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private final int redRating = 0;

	// Set up the column widths for the video table - avoiding magic numbers
    private final int nameAndSearchColWidth = 138;
    private final int columnWidthOther = 64;


    /***************************** FXML METHODS ********************************/

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

    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().switchScene("ReviewPlayer", "Review Videos", root.getScene());
    	ReviewController controller = reviewWindow.loader().getController();

    	// Set up the playlist of videos that will be played
    	ArrayList<VideoCreation> playList = new ArrayList<VideoCreation>();
    	playList.add(videoCreation);
    	controller.setPlaylist(playList);
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
            	updateVideoTable();
                updateVideosToReview();
                disableVideoButtons();
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
    private void reviewVideos() {
    	// Update videos to review
    	updateVideosToReview();

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
            disableVideoButtons();
        } else {
            enableVideoButtons();
        }
    }

    /**
     * Is run first on startup to set up the tableView, videos, and help buttons
     */
    @FXML
    private void initialize() {
    	stackPane.setPickOnBounds(false);
    	initTable();
    	updateVideosToReview();
        setUpHelp();
        updateVideoTable();
        checkVideosExist();
        disableVideoButtons();
    }


    /***************************** HELPER METHODS ********************************/

    /**
     * Refresh the video table with any updates (videos added, deleted, rating changes, etc.)
     */
    private void updateVideoTable() {
        videoTable.getItems().clear();
        videoTable.getItems().addAll(videoManager.getVideos());
        // Show user how many videos there are available
        int num = videoTable.getItems().size();
        if (num == 1) numVideoLabel.setText("There is currently " + num + " video!");
        else numVideoLabel.setText("There are currently " + num + " videos!");

        // When the video table is updated, see if there are any videos in it, and enable/disable buttons accordingly
        checkVideosExist();
    }

    /**
     * Initialise the video table (populate it with any available videos, and set the style)
     */
    @SuppressWarnings("unchecked")
    private void initTable() {
    	// Setup coloring of rows based on rating
        videoTable.setRowFactory(tv -> new TableRow<VideoCreation>() {
            @Override
            protected void updateItem(VideoCreation item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null | empty) {
                    getStyleClass().clear();
                    setStyle("");
                } else if (item.getRating() >= greenRating) {
                    getStyleClass().clear();
                    getStyleClass().add("green-row");
                } else if (item.getRating() >= yellowRating) {
                    getStyleClass().clear();
                    getStyleClass().add("yellow-row");
                } else {
                    getStyleClass().clear();
                    getStyleClass().add("red-row");
                }
            }
        });
	    // Set the colours of the video table's aspects
        videoTable.setStyle("-fx-selection-bar: blue; -fx-selection-bar-non-focused: purple;");
       
	    // Populate table with columns of parameters of videocreations (Favourite, Name, search term, #images, rating, views)
	    // Favourite column
        TableColumn<VideoCreation, String> favColumn = new TableColumn<>("Favourite");
        favColumn.setMinWidth(columnWidthOther);
        favColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));

        // Name column
        TableColumn<VideoCreation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(nameAndSearchColWidth);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	    
	    //Search term
        TableColumn<VideoCreation, String> searchTermColumn = new TableColumn<>("Search Term");
        searchTermColumn.setMinWidth(nameAndSearchColWidth);
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
	    
        // Number of images
        TableColumn<VideoCreation, String> numImagesColumn = new TableColumn<>("#Images");
        numImagesColumn.setMinWidth(columnWidthOther);
        numImagesColumn.setCellValueFactory(new PropertyValueFactory<>("numImages"));
	    
        // Rating
        TableColumn<VideoCreation, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(columnWidthOther);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
	    
	    // Number of views
        TableColumn<VideoCreation, String> viewsColumn = new TableColumn<>("Views");
        viewsColumn.setMinWidth(columnWidthOther);
        viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));
	    
        // Number of videos in the table
        videoTable.getItems().addAll(videoManager.getVideos());
        videoTable.getColumns().addAll(favColumn, nameColumn, searchTermColumn, numImagesColumn, ratingColumn, viewsColumn);
    	numVideoLabel.setText("There are " + videoTable.getItems().size() + " videos");
    }

    /**
     * Add on-hover help messages to the "?" buttons on the HomePage
     */
    private void setUpHelp() {
        helpTableView.setTooltip(new HoverToolTip("All of your video creations are listed here! The columns show you: \nthe video's name; \nthe word you searched; \nthe number of images in the video; \nthe rating out of 5 you gave each video; \nthe number of times you have watched each video.").getToolTip());

        helpCreateButton.setTooltip(new HoverToolTip("Click this button to start creating a new video! (Opens a new window)").getToolTip());

        helpDeleteButton.setTooltip(new HoverToolTip("Click a row to choose a video, then click this button to delete it!").getToolTip());

        helpHelp.setTooltip(new HoverToolTip("This is what the hover text will look like!").getToolTip());

        helpPlayButton.setTooltip(new HoverToolTip("Click a row to choose a video, then click this button to play it, and rate it!").getToolTip());

        helpQuitButton.setTooltip(new HoverToolTip("Click this button to exit the application!").getToolTip());

        helpReviewButton.setTooltip(new HoverToolTip("Click this to watch videos that you need to review, and to rate them!").getToolTip());

        helpVarPedia.setTooltip(new HoverToolTip("Welcome! This is VARpedia. \nThis application is made for you to learn new words by letting you create videos about them with pictures and text!").getToolTip());
    }

    /**
     * Update the list of videos that will be prompted to the user to review
     */
    private void updateVideosToReview() {
    	toReview.clear();
    	reviewNumAlert.setVisible(false);

    	// Add all videos with red ratings
    	for (VideoCreation v: videoManager.getVideos()) {
    		if (v.getRating() < yellowRating) {
    		    toReview.add(v);
            }
    	}
        if (toReview.size() != 0) {
            reviewNumAlert.setVisible(true);
            reviewNumAlert.setText("" + toReview.size());
        }
    	// If no videos with red ratings exist then review yellow ratings
    	if (toReview.size() == 0) {
    		for (VideoCreation v: videoManager.getVideos()) {
        		if (v.getRating() < greenRating) {
                    toReview.add(v);
                }
        	}
    		if (toReview.size() != 0) {
                reviewNumAlert.setVisible(true);
                reviewNumAlert.setText("" + toReview.size());
            }
    	}
    	// If no red or yellow ratings then review everything
    	if (toReview.size() == 0) {
    		toReview = new ArrayList<VideoCreation>(videoTable.getItems());
            if (toReview.size() != 0) {
                reviewNumAlert.setVisible(true);
                reviewNumAlert.setText("" + toReview.size());
            }
    	}
    }

    /**
     * Create a dialog (shown on startup) that presents a list of suggested videos for the user to review
     */
    public void remindReview() {
    	// Don't show dialog if there is nothing to review.
    	if (toReview.size() == 0) return;
    	String body = "";
    	body += "You should take a look at these videos:\n";
    	// Print the videos to review to the list
    	for(VideoCreation v: toReview) {
    		body += v.getName() + "\n";
    	}

    	// Create the dialog
    	Button review = new DialogBuilder().reminder(stackPane, "Review Reminder", body);
    	review.setOnAction(e-> {
    		reviewVideos();
    	});
    }

    /**
     * Disable the play, review, and delete buttons if no videos exist
     */
    private void checkVideosExist() {
        // Get the current number of videos in the table
        int numVideos = videoTable.getItems().size();

        // Disable/enable the buttons
        if (numVideos == 0) {
            disableVideoButtons();
        } else {
            enableVideoButtons();
        }
    }

    /**
     * Helper method that disables the play. delete, and review buttons.
     * Note the review button doesn't get disabled if the video table is populated
     */
    private void disableVideoButtons() {
        playButton.setDisable(true);
        deleteButton.setDisable(true);
        reviewButton.setDisable(true);
        if (videoTable.getItems().size() != 0) reviewButton.setDisable(false);
    }

    /**
     * Helper method that enables the play and delete buttons
     */
    private void enableVideoButtons() {
        playButton.setDisable(false);
        deleteButton.setDisable(false);
    }

}
