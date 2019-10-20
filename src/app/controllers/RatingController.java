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
	@FXML private JFXButton helpConfirm;
	@FXML private JFXButton helpCancel;
	
	// Set up rating
	private Integer rating;
	
	// Set up colour scheme for selected rating star
	private final String selected = "-fx-fill:rgb(233.0,195.0,248.0);";
	private final String unselected = "-fx-fill:black;";


	/***************************** FXML METHODS - ENTER  ********************************/

	/**
	 * If first star was selected, show this on the popup
	 */
	@FXML
	private void star1Enter() {
		unselectAll();
        star1.setStyle(selected);
	}

	/**
	 * If second star was selected, show this on the popup
	 */
	@FXML
	private void star2Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
	}

	/**
	 * If third star was selected, show this on the popup
	 */
	@FXML
	private void star3Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
	}

	/**
	 * If fourth star was selected, show this on the popup
	 */
	@FXML
	private void star4Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
        star4.setStyle(selected);
	}

	/**
	 * If fifth star was selected, show this on the popup
	 */
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

	/**
	 * On exit, unselect all stars
	 */
	@FXML
	private void star1Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}

	/**
	 * On exit, unselect all stars
	 */
	@FXML
	private void star2Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}

	/**
	 * On exit, unselect all stars
	 */
	@FXML
	private void star3Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}

	/**
	 * On exit, unselect all stars
	 */
	@FXML
	private void star4Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}

	/**
	 * On exit, unselect all stars
	 */
	@FXML
	private void star5Exit() {
		unselectAll();
		if (rating != null) clickPrev();
	}


	/***************************** FXML METHODS - CLICK ********************************/

	/**
	 * Update the rating - set it to 1 - based on the first star being clicked
	 */
	@FXML
	private void star1Click() {
		star1Enter();
		rating = 1;
        updateRatingLabel();
	}

	/**
	 * Update the rating - set it to 2 - based on the second star being clicked
	 */
	@FXML
	private void star2Click() {
		star2Enter();
		rating = 2;
        updateRatingLabel();
	}

	/**
	 * Update the rating - set it to 3 - based on the third star being clicked
	 */
	@FXML
	private void star3Click() {
		star3Enter();
		rating = 3;
        updateRatingLabel();
	}

	/**
	 * Update the rating - set it to 4 - based on the fourth star being clicked
	 */
	@FXML
	private void star4Click() {
		star4Enter();
		rating = 4;
        updateRatingLabel();
	}

	/**
	 * Update the rating - set it to 5 - based on the fifth star being clicked
	 */
	@FXML
	private void star5Click() {
		star5Enter();
		rating = 5;
        updateRatingLabel();
	}

	/**
	 * Delete rating and leave rating scene
	 */
	@FXML
	private void cancel() {
		rating = null;
		confirm();
	}

	/**
	 * Quit rating scene with new rating saved
	 */
	@FXML
	private void confirm() {
		root.getScene().getWindow().hide();
	}

	/**
	 * Set up tooltips when the window is opened
	 */
	@FXML
	private void initialize() {
		setUpHelp();
	}


	/***************************** HELPER METHODS ********************************/

	/**
	 * Unselect all stars on the rating popup
	 */
	private void unselectAll() {
		star1.setStyle(unselected);
		star2.setStyle(unselected);
		star3.setStyle(unselected);
		star4.setStyle(unselected);
		star5.setStyle(unselected);
	}

	/**
	 * Getter for user's rating
	 * @return the rating as an integer
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Set the tooltip's text
	 */
	private void setUpHelp() {
		helpRating.setTooltip(new HoverToolTip("Click on the stars below to rate how well you understood the video. \nThis rating is out of 5, with 5 being you fully understand!").getToolTip());

		helpCancel.setTooltip(new HoverToolTip("Click this if you don't want to rate the video, and go back to the video player!").getToolTip());

		helpConfirm.setTooltip(new HoverToolTip("Click this if you are done with rating the video, and want to save it and go back to the video player!").getToolTip());
	}

	/**
	 * Update the rating label with the new rating based on what star was selected
	 */
	private void updateRatingLabel() {
		ratingLabel.setText("Rating: " + rating);
	}

	/**
	 * Show the previous rating for the video as selected stars on the rating popup
	 */
	private void clickPrev() {
		if(rating == null) return;
		if (rating.equals(1)) star1Click();
		if (rating.equals(2)) star2Click();
		if (rating.equals(3)) star3Click();
		if (rating.equals(4)) star4Click();
		if (rating.equals(5)) star5Click();
	}

}
