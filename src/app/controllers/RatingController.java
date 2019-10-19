package app.controllers;

import com.jfoenix.controls.JFXButton;

import app.DraggableWindow;
import app.HoverToolTip;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for the Rating scene - is a draggable window
 */
public class RatingController extends DraggableWindow {

	/***************************** FIELD DECLARATIONS ********************************/
	// Root of scene
	@FXML private AnchorPane root;
	
	// Star icons
	@FXML private MaterialDesignIconView star1;
	@FXML private MaterialDesignIconView star2;
	@FXML private MaterialDesignIconView star3;
	@FXML private MaterialDesignIconView star4;
	@FXML private MaterialDesignIconView star5;
	
	// Labels and help buttons
	@FXML private Label ratingLabel;
	@FXML private JFXButton helpRating;
	
	// Set up rating
	private Integer rating;
	
	// Set up colour scheme for selected rating star
	private final String selected = "-fx-fill:rgb(233.0,195.0,248.0);";
	private final String unselected = "-fx-fill:black;";
	
	// Unselect all the stars
	private void unselectAll() {
		star1.setStyle(unselected);
		star2.setStyle(unselected);
		star3.setStyle(unselected);
		star4.setStyle(unselected);
		star5.setStyle(unselected);
	}


	/***************************** FXML METHODS - ENTER  ********************************/

	// If star 1 was selected
	@FXML
	private void star1Enter() {
		unselectAll();
        star1.setStyle(selected);
	}
	
	// If star 2 was selected
	@FXML
	private void star2Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
	}
	
	// If star 3 was selected
	@FXML
	private void star3Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
	}
	
	// If star 4 was selected
	@FXML
	private void star4Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
        star4.setStyle(selected);
	}
	
	// If star 5 was selected
	@FXML
	private void star5Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
        star4.setStyle(selected);
        star5.setStyle(selected);
	}


	/***************************** FXML METHODS - EXIT ********************************/

	// Unselect all stars regardless of star selected
	@FXML
	private void star1Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}
	@FXML
	private void star2Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}
	@FXML
	private void star3Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}
	@FXML
	private void star4Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}
	@FXML
	private void star5Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}


	/***************************** FXML METHODS - CLICK ********************************/

	// Update rating based on which star selected
	@FXML
	private void star1Click() {
		star1Enter();
		rating = 1;
        updateRatingLabel();
	}
	@FXML
	private void star2Click() {
		star2Enter();
		rating = 2;
        updateRatingLabel();
	}
	@FXML
	private void star3Click() {
		star3Enter();
		rating = 3;
        updateRatingLabel();
	}
	@FXML
	private void star4Click() {
		star4Enter();
		rating = 4;
        updateRatingLabel();
	}
	@FXML
	private void star5Click() {
		star5Enter();
		rating = 5;
        updateRatingLabel();
	}
	
	// Delete rating and leave rating scene
	@FXML
	private void cancel() {
		rating = null;
		confirm();
	}
	
	// Quit rating scene with new rating
	@FXML
	private void confirm() {
		root.getScene().getWindow().hide();
	}

	// Set up the tooltips when the scene is opened
	@FXML
	private void initialize() {
		setUpHelp();
	}


	/***************************** HELPER METHODS ********************************/

	// Getter for user's rating
	public Integer getRating() {
		return rating;
	}

	// Set the tooltip's text
	private void setUpHelp() {
		helpRating.setTooltip(new HoverToolTip("Click on the stars below to set how well you think you understand the video you just watched. \nThis rating is out of 5, with 5 being you fully understand! \nClick Confirm when you're done, or cancel if you don't want to rate the video!").getToolTip());
	}

	// Update the rating label with the new rating based on what star was selected
	private void updateRatingLabel() {
		ratingLabel.setText("Rating: " + rating);
	}

	// Show the previous rating for the video as selected stars
	private void clickPrev() {
		if(rating == null) return;
		if (rating.equals(1)) star1Click();
		if (rating.equals(2)) star2Click();
		if (rating.equals(3)) star3Click();
		if (rating.equals(4)) star4Click();
		if (rating.equals(5)) star5Click();
	}

}
