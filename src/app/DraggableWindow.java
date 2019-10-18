package app;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
* Make a window click-and-draggable 
*/
public class DraggableWindow {
	
	@FXML
	private AnchorPane root;
    double xOffset = 0;
    double yOffset = 0;
    
    public void makeStageDrageable() {
    	Stage stage = (Stage) root.getScene().getWindow();
        root.setOnMousePressed(event-> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event-> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
                stage.setOpacity(0.8f);
        });
        root.setOnDragDone((e) -> {
            stage.setOpacity(1.0f);
        });
        root.setOnMouseReleased((e) -> {
            stage.setOpacity(1.0f);
        });
    }
}
