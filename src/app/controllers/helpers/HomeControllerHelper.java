package app.controllers.helpers;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import app.DialogBuilder;
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

/**
 * Handles functionality for the HomeController class
 */
public class HomeControllerHelper {

    // Field declarations
	private HomeController controller;

    /**
     * Constructor so the that controller class has non-static access to the helper methods in this class
     * @param controller
     */
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
        if (numVideos == 0) disableVideoButtons(playButton, deleteButton, reviewButton, videoTable);
        else enableVideoButtons(playButton, deleteButton);
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
     * Update the list of videos that will be prompted to the user to review
     */
    public void updateVideosToReview(ArrayList<VideoCreation> toReview, VideoManager videoManager, JFXButton reviewNumAlert, TableView videoTable, int greenRating, int yellowRating) {
    	toReview.clear();
    	reviewNumAlert.setVisible(false);

    	// Add all videos with red ratings
    	for (VideoCreation v: videoManager.getVideos()) {
    		if (v.getRating() < yellowRating) toReview.add(v);
    	}
    	// If no videos with red ratings exist then review yellow ratings
    	if (toReview.isEmpty()) {
    		for (VideoCreation v: videoManager.getVideos()) {
        		if (v.getRating() < greenRating) toReview.add(v);
        	}
    	}
    	// If no red or yellow ratings then review everything
    	if (toReview.isEmpty()) {
    		toReview = new ArrayList<VideoCreation>(videoTable.getItems());
    	}
		updateReviewAlert(toReview, reviewNumAlert);
    }
    
    
    /**
     * Shows the review alert if there are videos to review.
     */
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
    
    /**
     * Color the rows of the video table depending on the rating of the video
     */
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
    
    /**
     * 
     */
    public ArrayList<TableColumn<VideoCreation, String>> setUpOtherColumns(int nameAndSearchColWidth, int columnWidthOther) {
    	ArrayList<TableColumn<VideoCreation,String>> columns = new ArrayList<>();
    	
	    columns.add(buildColumn("Name", "name", nameAndSearchColWidth));
	    columns.add(buildColumn("Search Term", "searchTerm", nameAndSearchColWidth));
	    columns.add(buildColumn("#Images", "numImages", columnWidthOther));
	    columns.add(buildColumn("Rating", "rating", columnWidthOther));
	    columns.add(buildColumn("Views", "views", columnWidthOther));
        return columns;
    }
    
    /**
     * Set up the favourites column, and make the favourite icon clickable
     */
    public TableColumn<VideoCreation, MaterialDesignIconView> setUpFavColumn(TableView videoTable, VideoManager videoManager, Label numVideoLabel, 
    							JFXButton playButton, JFXButton deleteButton, JFXButton reviewButton, int favouriteColWidth) {
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
        return favColumn;
    }
    
    /**
     * Make a table column with specified params
     */
    private TableColumn<VideoCreation, String> buildColumn(String columnName, String parameter, int columnWidth) {
    	TableColumn<VideoCreation, String> newColumn = new TableColumn<>(columnName);
    	newColumn.setMinWidth(columnWidth);
    	newColumn.setCellValueFactory(new PropertyValueFactory<>(parameter));
        return newColumn;
    }

    /**
     * Initialise the video table (populate it with any available videos, and set the style)
     */
    public void initTable(TableView videoTable, VideoManager videoManager, Label numVideoLabel, JFXButton playButton, JFXButton deleteButton, 
    	JFXButton reviewButton, int favouriteColWidth, int greenRating, int yellowRating, int nameAndSearchColWidth, int columnWidthOther) {
    	colorVideoTable(videoTable, greenRating, yellowRating);
	    // Set the colours of the video table's aspects
        videoTable.setStyle("-fx-selection-bar: blue; -fx-selection-bar-non-focused: purple;");
        // Populate the video table
        videoTable.getItems().addAll(videoManager.getVideos());
        videoTable.getColumns().add(setUpFavColumn(videoTable, videoManager, numVideoLabel, playButton, deleteButton, 
				reviewButton, favouriteColWidth));
        videoTable.getColumns().addAll(setUpOtherColumns(nameAndSearchColWidth, columnWidthOther));
    	numVideoLabel.setText("There are " + videoTable.getItems().size() + " videos");
    }

}
