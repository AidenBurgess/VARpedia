package app.controllers.helpers;

import app.controllers.HomeController;
import app.controllers.RatingController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.control.Label;

/**
 * Class providing helper methods providing functionality for RatingController
 */
public class RatingControllerHelper {

    // Field declarations
    private RatingController controller;
    private final String unselected = "-fx-fill:black;";

    /**
     * Constructor so that the RatingController has non-static access to the helper methods in this class
     */
    public RatingControllerHelper(RatingController controller) {
        this.controller=controller;
    }

    /**
     * Show the previous rating for the video as selected stars on the rating popup
     */
    public void clickPrev(Integer rating) {
        if(rating == null) return;
        if (rating.equals(1)) controller.star1Click();
        if (rating.equals(2)) controller.star2Click();
        if (rating.equals(3)) controller.star3Click();
        if (rating.equals(4)) controller.star4Click();
        if (rating.equals(5)) controller.star5Click();
    }

    /**
     * Unselect all stars on the rating popup
     */
    public void unselectAll(MaterialDesignIconView star1, MaterialDesignIconView star2, MaterialDesignIconView star3, MaterialDesignIconView star4, MaterialDesignIconView star5) {
        star1.setStyle(unselected);
        star2.setStyle(unselected);
        star3.setStyle(unselected);
        star4.setStyle(unselected);
        star5.setStyle(unselected);
    }

    /**
     * Update the rating label with the new rating based on which star was selected
     */
    public void updateRatingLabel(Label ratingLabel, Integer rating) {
        ratingLabel.setText("Rating: " + rating);
    }

}
