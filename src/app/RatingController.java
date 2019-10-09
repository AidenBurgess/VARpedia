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
	private FontAwesomeIcon star4;
	@FXML
	private FontAwesomeIcon star5;
	@FXML
	private Label ratingLabel;
	
	private Integer rating;
	private VideoCreation videoCreation;
	private String selected = "yellow";
	private String unselected = "black";

	public void setVideo(VideoCreation videoCreation) {
		this.videoCreation = videoCreation;
	}
	
	@FXML
	private void initialize() {
		
	}
	
	private void unselectAll() {
		star1.setStyle("-fx-fill:" + unselected + ";");
		star2.setStyle("-fx-fill:" + unselected + ";");
		star3.setStyle("-fx-fill:" + unselected + ";");
		star4.setStyle("-fx-fill:" + unselected + ";");
		star5.setStyle("-fx-fill:" + unselected + ";");
	}
	
	@FXML
	private void star1Enter() {
		unselectAll();
        star1.setStyle("-fx-fill:" + selected +";");
	}
	@FXML
	private void star2Enter() {
		unselectAll();
        star1.setStyle("-fx-fill:" + selected +";");
        star2.setStyle("-fx-fill:" + selected +";");
	}
	@FXML
	private void star3Enter() {
		unselectAll();
        star1.setStyle("-fx-fill:" + selected +";");
        star2.setStyle("-fx-fill:" + selected +";");
        star3.setStyle("-fx-fill:" + selected +";");
	}
	@FXML
	private void star4Enter() {
		unselectAll();
        star1.setStyle("-fx-fill:" + selected +";");
        star2.setStyle("-fx-fill:" + selected +";");
        star3.setStyle("-fx-fill:" + selected +";");
        star4.setStyle("-fx-fill:" + selected +";");
	}
	@FXML
	private void star5Enter() {
		unselectAll();
        star1.setStyle("-fx-fill:" + selected +";");
        star2.setStyle("-fx-fill:" + selected +";");
        star3.setStyle("-fx-fill:" + selected +";");
        star4.setStyle("-fx-fill:" + selected +";");
        star5.setStyle("-fx-fill:" + selected +";");
	}
	
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
}
