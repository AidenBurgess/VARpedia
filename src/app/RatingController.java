package app;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class RatingController {
	
	@FXML
	private AnchorPane root;
	@FXML
	private MaterialDesignIconView star1;
	@FXML
	private MaterialDesignIconView star2;
	@FXML
	private MaterialDesignIconView star3;
	@FXML
	private MaterialDesignIconView star4;
	@FXML
	private MaterialDesignIconView star5;
	@FXML
	private Label ratingLabel;
	
	private Integer rating;
	private String selected = "-fx-fill:lightblue;";
	private String unselected = "-fx-fill:black;";
	
	private void unselectAll() {
		star1.setStyle(unselected);
		star2.setStyle(unselected);
		star3.setStyle(unselected);
		star4.setStyle(unselected);
		star5.setStyle(unselected);
	}
	
	/*********************ENTER FUNCTIONALITY***********************/
	@FXML
	private void star1Enter() {
		unselectAll();
        star1.setStyle(selected);
	}
	@FXML
	private void star2Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
	}
	@FXML
	private void star3Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
	}
	@FXML
	private void star4Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
        star4.setStyle(selected);
	}
	@FXML
	private void star5Enter() {
		unselectAll();
        star1.setStyle(selected);
        star2.setStyle(selected);
        star3.setStyle(selected);
        star4.setStyle(selected);
        star5.setStyle(selected);
	}
	/*********************EXIT FUNCTIONALITY***********************/
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
	
	/*********************CLICK FUNCTIONALITY***********************/
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
	
	private void updateRatingLabel() {
		ratingLabel.setText("Rating: " + rating);
	}
	
	private void clickPrev() {
		if(rating == null) return;
		if (rating.equals(1)) star1Click();
		if (rating.equals(2)) star2Click();
		if (rating.equals(3)) star3Click();
		if (rating.equals(4)) star4Click();
		if (rating.equals(5)) star5Click();
	}
	
	@FXML
	private void cancel() {
		rating = null;
		confirm();
	}
	
	@FXML
	private void confirm() {
		System.out.println("User rating is: " + rating);
		root.getScene().getWindow().hide();
	}
	
	public Integer getRating() {
		return rating;
	}
}
