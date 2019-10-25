package app.controllers.helpers;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import app.DialogBuilder;
import app.HoverToolTip;
import app.VideoCreation;
import app.VideoManager;
import app.controllers.HomeController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class HomeControllerHelper {
	
	HomeController controller;
	
	public HomeControllerHelper(HomeController controller) {
		this.controller = controller;
	}
	
    /**
     * Disable the play, review, and delete buttons if no videos exist
     */
    public void checkVideosExist(JFXButton playButton, JFXButton deleteButton, JFXButton reviewButton, TableView videoTable) {
        // Get the current number of videos in the table
        int numVideos = videoTable.getItems().size();
        // Disable/enable the buttons
        if (numVideos == 0) {
            disableVideoButtons(playButton, deleteButton, reviewButton, videoTable);
        } else {
            enableVideoButtons(playButton, deleteButton);
        }
    }
	
    /**
     * Helper method that disables the play. delete, and review buttons.
     * Note the review button doesn't get disabled if the video table is populated
     */
    public void disableVideoButtons(JFXButton playButton, JFXButton deleteButton, JFXButton reviewButton, TableView videoTable) {
        playButton.setDisable(true);
        deleteButton.setDisable(true);
        reviewButton.setDisable(true);
        if (videoTable.getItems().size() != 0) reviewButton.setDisable(false);
    }
    
    /**
     * Helper method that enables the play and delete buttons
     */
    public void enableVideoButtons(JFXButton playButton, JFXButton deleteButton) {
        playButton.setDisable(false);
        deleteButton.setDisable(false);
    }
    
    /**
     * Create a dialog that presents a list of suggested videos for the user to review
     */
    public void remindReview(ArrayList<VideoCreation> toReview, StackPane stackPane) {
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
    		controller.reviewVideos();
    	});
    }
    
    /**
     * Add on-hover help messages to the "?" buttons on the HomePage
     */
    public void setUpHelp(JFXButton helpTableView, JFXButton helpCreateButton, JFXButton helpDeleteButton, JFXButton helpHelp, JFXButton helpPlayButton, JFXButton helpQuitButton, JFXButton helpReviewButton, JFXButton helpVarPedia) {
        helpTableView.setTooltip(new HoverToolTip("All of your video creations are listed here! The columns show you: \n"
        		+ "if the video is a favourite; \nthe video's name; \nthe word you searched; "
        		+ "\nthe number of images in the video; \nthe rating out of 5 you gave each video; "
        		+ "\nthe number of times you have watched each video.").getToolTip());
        helpCreateButton.setTooltip(new HoverToolTip("Click this button to start creating a new video! (Opens a new window)").getToolTip());
        helpDeleteButton.setTooltip(new HoverToolTip("Click a row to choose a video, then click this button to delete it!").getToolTip());
        helpHelp.setTooltip(new HoverToolTip("This is what the hover text will look like!").getToolTip());
        helpPlayButton.setTooltip(new HoverToolTip("Click a row to choose a video, then click this button to play it, and rate it or add it to your favourites!").getToolTip());
        helpQuitButton.setTooltip(new HoverToolTip("Click this button to exit the application!").getToolTip());
        helpReviewButton.setTooltip(new HoverToolTip("Click this to watch videos that you need to review, and to rate them or add them to your favourites!").getToolTip());
        helpVarPedia.setTooltip(new HoverToolTip("Welcome! This is VARpedia. \nThis application is made for you to learn new words by letting you create videos about them with pictures and text!").getToolTip());
    }
    
    /**
     * Update the list of videos that will be prompted to the user to review
     */
    public void updateVideosToReview(ArrayList<VideoCreation> toReview, VideoManager videoManager, JFXButton reviewNumAlert, TableView videoTable, int greenRating, int yellowRating) {
    	toReview.clear();
    	reviewNumAlert.setVisible(false);

    	// Add all videos with red ratings
    	for (VideoCreation v: videoManager.getVideos()) {
    		if (v.getRating() < yellowRating) toReview.add(v);
    		updateReviewAlert(toReview, reviewNumAlert);
    	}
    	// If no videos with red ratings exist then review yellow ratings
    	if (toReview.isEmpty()) {
    		for (VideoCreation v: videoManager.getVideos()) {
        		if (v.getRating() < greenRating) toReview.add(v);
        	}
    		updateReviewAlert(toReview, reviewNumAlert);
    	}
    	// If no red or yellow ratings then review everything
    	if (toReview.isEmpty()) {
    		toReview = new ArrayList<VideoCreation>(videoTable.getItems());
    		updateReviewAlert(toReview, reviewNumAlert);
    	}
    }
    
    void updateReviewAlert(ArrayList toReview, JFXButton reviewNumAlert) {
    	if (toReview.size() != 0) {
            reviewNumAlert.setVisible(true);
            reviewNumAlert.setText("" + toReview.size());
        }
    }
    
    /**
     * Refresh the video table with any updates (videos added, deleted, rating changes, etc.)
     */
    public void updateVideoTable(TableView videoTable, VideoManager videoManager, Label numVideoLabel, JFXButton playButton, JFXButton deleteButton, JFXButton reviewButton) {
        videoTable.getItems().clear();
        videoTable.getItems().addAll(videoManager.getVideos());
        // Show user how many videos there are available
        int num = videoTable.getItems().size();
        if (num == 1) numVideoLabel.setText("There is currently " + num + " video!");
        else numVideoLabel.setText("There are currently " + num + " videos!");
        // When the video table is updated, see if there are any videos in it, and enable/disable buttons accordingly
        checkVideosExist(playButton, deleteButton, reviewButton, videoTable);
    }
    
    public void colorVideoTable(TableView videoTable, int greenRating, int yellowRating) {
    	videoTable.setRowFactory(tv -> new TableRow<VideoCreation>() {
            @Override
            protected void updateItem(VideoCreation item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().clear();
                if (item == null | empty) setStyle("");
                else if (item.getRating() >= greenRating) getStyleClass().add("green-row");
                else if (item.getRating() >= yellowRating) getStyleClass().add("yellow-row");
                else getStyleClass().add("red-row");
            }
        });
    }
    
    public ArrayList<TableColumn<VideoCreation, String>> setUpColumns(int nameAndSearchColWidth, int columnWidthOther) {
    	ArrayList<TableColumn<VideoCreation,String>> columns = new ArrayList<>();
    	// Name column
        TableColumn<VideoCreation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(nameAndSearchColWidth);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	    columns.add(nameColumn);
	    //Search term
        TableColumn<VideoCreation, String> searchTermColumn = new TableColumn<>("Search Term");
        searchTermColumn.setMinWidth(nameAndSearchColWidth);
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
	    columns.add(searchTermColumn);
        // Number of images
        TableColumn<VideoCreation, String> numImagesColumn = new TableColumn<>("#Images");
        numImagesColumn.setMinWidth(columnWidthOther);
        numImagesColumn.setCellValueFactory(new PropertyValueFactory<>("numImages"));
	    columns.add(numImagesColumn);
        // Rating
        TableColumn<VideoCreation, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(columnWidthOther);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
	    columns.add(ratingColumn);
	    // Number of views
        TableColumn<VideoCreation, String> viewsColumn = new TableColumn<>("Views");
        viewsColumn.setMinWidth(columnWidthOther);
        viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));
	    columns.add(viewsColumn);

        return columns;
    }
    

    /**
     * Initialise the video table (populate it with any available videos, and set the style)
     */
    public void initTable(TableView videoTable, VideoManager videoManager, Label numVideoLabel, JFXButton playButton, JFXButton deleteButton, 
    		JFXButton reviewButton, int favouriteColWidth, int greenRating, int yellowRating, int nameAndSearchColWidth, int columnWidthOther) {
    	// Setup coloring of rows based on rating
    	colorVideoTable(videoTable, greenRating, yellowRating);
        
	    // Set the colours of the video table's aspects
        videoTable.setStyle("-fx-selection-bar: blue; -fx-selection-bar-non-focused: purple;");
       
	    // Populate table with columns of parameters of videocreations (Favourite, Name, search term, #images, rating, views)
	    // Favourite column - Will show a heart icon if it is a favourite
        TableColumn<VideoCreation, MaterialDesignIconView> favColumn = new TableColumn<>("Favourite");
        favColumn.setMinWidth(favouriteColWidth);
        favColumn.setCellValueFactory(c -> {
            VideoCreation favourite = c.getValue();
            MaterialDesignIconView imageFav;
            if (favourite.getFavourite()) {
                imageFav = new MaterialDesignIconView(MaterialDesignIcon.HEART);
            } else {
                imageFav = new MaterialDesignIconView(MaterialDesignIcon.HEART_OUTLINE);
            }
            imageFav.getStyleClass().add("fav-icon");
            imageFav.setOnMouseClicked(e-> {
            	favourite.toggleFavourite();
                updateVideoTable(videoTable, videoManager, numVideoLabel, playButton, deleteButton,  reviewButton);
            });
            return new SimpleObjectProperty<>(imageFav);
        });
	    
        // Number of videos in the table
        videoTable.getItems().addAll(videoManager.getVideos());
        videoTable.getColumns().addAll(favColumn);
        videoTable.getColumns().addAll(setUpColumns(nameAndSearchColWidth, columnWidthOther));
    	numVideoLabel.setText("There are " + videoTable.getItems().size() + " videos");
    }

}
