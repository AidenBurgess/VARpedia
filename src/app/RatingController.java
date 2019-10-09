package app;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class RatingController {
	
	@FXML
	private AnchorPane root;
	@FXML
	private FontAwesomeIcon star1;
	@FXML
	private FontAwesomeIcon star2;
	@FXML
	private FontAwesomeIcon star3;
	@FXML
	private Label ratingLabel;
	
	private Integer click;
	private VideoCreation videoCreation;

	public void setVideo(VideoCreation videoCreation) {
		this.videoCreation = videoCreation;
	}

	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void star1Enter() {
        star1.setStyle("-fx-fill:yellow;");
		star2.setStyle("-fx-fill:black;");
		star3.setStyle("-fx-fill:black;");
	}
	@FXML
	private void star2Enter() {
        star1.setStyle("-fx-fill:yellow;");
        star2.setStyle("-fx-fill:yellow;");
		star3.setStyle("-fx-fill:black;");
	}
	@FXML
	private void star3Enter() {
        star1.setStyle("-fx-fill:yellow;");
        star2.setStyle("-fx-fill:yellow;");
        star3.setStyle("-fx-fill:yellow;");
	}
	
	@FXML
	private void star1Exit() {
		star1.setStyle("-fx-fill:black;");
		star2.setStyle("-fx-fill:black;");
		star3.setStyle("-fx-fill:black;");
		if (click == null) return;
		clickPrev();
	}
	@FXML
	private void star2Exit() {
		star1.setStyle("-fx-fill:black;");
		star2.setStyle("-fx-fill:black;");
		star3.setStyle("-fx-fill:black;");
		if (click == null) return;
		clickPrev();
	}
	@FXML
	private void star3Exit() {
		star1.setStyle("-fx-fill:black;");
		star2.setStyle("-fx-fill:black;");
		star3.setStyle("-fx-fill:black;");
		if (click == null) return;
		clickPrev();
	}
	
	@FXML
	private void star1Click() {
		star1Enter();
        click = 1;
        updateRatingLabel();
	}
	@FXML
	private void star2Click() {
		star2Enter();
        click = 2;
        updateRatingLabel();
	}
	@FXML
	private void star3Click() {
		star3Enter();
        click = 3;
        updateRatingLabel();
	}
	
	private void updateRatingLabel() {
		ratingLabel.setText("Rating: " + click);
	}

	
	private void clickPrev() {
		if(click == null) return;
		if (click.equals(1)) star1Click();
		if (click.equals(2)) star2Click();
		if (click.equals(3)) star3Click();
	}

	private void showRating() {
		System.out.println("Show rating");
	}

}
