package app.controllers;

import app.controllers.helpers.RatingControllerHelper;
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

	// Set up helper object to gain access to helper methods for this class
	private RatingControllerHelper helper = new RatingControllerHelper(this);


	/***************************** FXML METHODS - ENTER  ********************************/

	/**
	 * If first star was selected, show this on the popup
	 */
	@FXML
	private void star1Enter() {
		helper.unselectAll(star1, star2, star3, star4, star5);
        star1.setStyle(selected);
	}

	/**
	 * If second star was selected, show this on the popup
	 */
	@FXML
	private void star2Enter() {
		helper.unselectAll(star1, star2, star3, star4, star5);
        star1.setStyle(selected);
        star2.setStyle(selected);
	}

	/**
	 * If third star was selected, show this on the popup
	 */
	@FXML
	private void star3Enter() {
		helper.unselectAll(star1, star2, star3, star4, star5);
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
	}

	/**
	 * If fourth star was selected, show this on the popup
	 */
	@FXML
	private void star4Enter() {
		helper.unselectAll(star1, star2, star3, star4, star5);
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
		helper.unselectAll(star1, star2, star3, star4, star5);
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
	private void starExit() {
		helper.unselectAll(star1, star2, star3, star4, star5);
		if (rating != null) helper.clickPrev(rating);
	}

	/***************************** FXML METHODS - CLICK ********************************/

	/**
	 * Update the rating - set it to 1 - based on the first star being clicked
	 */
	@FXML
	public void star1Click() {
		star1Enter();
		rating = 1;
        helper.updateRatingLabel(ratingLabel, rating);
	}

	/**
	 * Update the rating - set it to 2 - based on the second star being clicked
	 */
	@FXML
	public void star2Click() {
		star2Enter();
		rating = 2;
		helper.updateRatingLabel(ratingLabel, rating);
	}

	/**
	 * Update the rating - set it to 3 - based on the third star being clicked
	 */
	@FXML
	public void star3Click() {
		star3Enter();
		rating = 3;
		helper.updateRatingLabel(ratingLabel, rating);
	}

	/**
	 * Update the rating - set it to 4 - based on the fourth star being clicked
	 */
	@FXML
	public void star4Click() {
		star4Enter();
		rating = 4;
		helper.updateRatingLabel(ratingLabel, rating);
	}

	/**
	 * Update the rating - set it to 5 - based on the fifth star being clicked
	 */
	@FXML
	public void star5Click() {
		star5Enter();
		rating = 5;
		helper.updateRatingLabel(ratingLabel, rating);
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

}
