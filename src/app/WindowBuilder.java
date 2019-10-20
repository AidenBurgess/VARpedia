package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * Class that sets up and creates a window, and displays it
 */
public class WindowBuilder {

    // Field declarations
	private FXMLLoader loader;
	private Stage stage;
	private Scene scene;

    /**
     * Create a window according to an FXML file (whose name is retrieved through the screenName parameter), with no top bar, and make it appear on screen
     * @param screenName
     * @param title
     */
    public WindowBuilder noTop(String screenName, String title) {
        try {
            // Load the given FXML file and apply it to the scene and stage
            loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("resources/" + screenName + ".fxml"));
            scene = null;
            scene = new Scene(loader.load());
            stage = new Stage();

            // Style the stage
   		 	stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Make the window appear on screen
            stage.show();

            // Make the stage draggable
            ((DraggableWindow) loader.getController()).makeStageDrageable();

            // Return the resulting window
            return this;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Switch from the current window to a new one
     * @param screenName : name of an FXML file to apply to the new window
     * @param title
     * @param scene
     * @return
     */
    public WindowBuilder switchScene(String screenName, String title, Scene scene) {
        // Load the new FXML file
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("resources/" + screenName + ".fxml"));
        try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Set the new Scene
        scene.setRoot(loader.getRoot());
        scene.getWindow().sizeToScene();

        // Make the window draggable
    	((DraggableWindow) loader.getController()).makeStageDrageable();

    	// Return the created window
        return this;
    }

    /**
     * @return the FXML loader
     */
    public FXMLLoader loader() {
    	return loader;
    }

    /**
     * @return the stage
     */
    public Stage stage() {
		return stage;
	}
    /**
     * @return the scene
     */
    public Scene scene() {
		return scene;
	}
    /**
     * @return the FXML loader's controller
     */
    public Object controller() {
    	return loader.getController();
    }

}