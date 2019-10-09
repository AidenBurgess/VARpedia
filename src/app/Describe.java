package app;

import javafx.scene.control.Alert;

public class Describe {
    private String toDescribe;

    public Describe(String toDescribe) {
        this.toDescribe = toDescribe;
    }

    protected void describeSearchResults() {
        alerter("Help", "Search Results", this.toDescribe);
    }

    protected void describeAddRemoveText() {
        alerter("Help", "Add or Remove Text", this.toDescribe);
    }

    protected void describeShuffleText() {
        alerter("Help", "Shuffle Text", this.toDescribe);
    }

    protected void describeBackButton() {
        alerter("Help", "Back Button", this.toDescribe);
    }

    protected void describeSearchFunction() {
        alerter("Help", "Search Function", this.toDescribe);
    }

    protected void describeChooseVoice() {
        alerter("Help", "Choose Voice", this.toDescribe);
    }

    protected void describeVideoNameField() {
        alerter("Help", "Video Name Field", this.toDescribe);
    }

    protected void describeImagesSlider() {
        alerter("Help", "Number of Images Slider", this.toDescribe);
    }

    protected void describeCreateButton() {
        alerter("Help", "Create Button", this.toDescribe);
    }

    protected void describeTextList() {
        alerter("Help", "List of Text", this.toDescribe);
    }

    private void alerter(String title, String head, String desc) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(desc);
        alert.showAndWait();
    }

}
