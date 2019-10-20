package app;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
* Make a window click-and-draggable 
*/
public class DraggableWindow {

	// Field declarations
	@FXML private AnchorPane root;
    double xOffset = 0;
    double yOffset = 0;

    /**
     * Make the current stage draggable
     */
    public void makeStageDrageable() {
        // Get the current stage
    	Stage stage = (Stage) root.getScene().getWindow();

    	// Methods that handle the positions of the window as it is dragged
    	root.setOnMousePressed(event-> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event-> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
                // Change opacity of window as it is dragged
                stage.setOpacity(0.8f);
        });

        // Restore old opacity of the window when it is finished being dragged
        root.setOnDragDone((e) -> {
            stage.setOpacity(1.0f);
        });
        root.setOnMouseReleased((e) -> {
            stage.setOpacity(1.0f);
        });
    }
}
