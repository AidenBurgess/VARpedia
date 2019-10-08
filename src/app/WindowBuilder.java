package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowBuilder {
	
	private FXMLLoader loader;
	private Stage stage;

    public WindowBuilder popWindow(String screenName, String title) {
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(screenName + ".fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();

        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        return this;
    }
    
    public FXMLLoader getLoader() {
    	return loader;
    }
    
    public Stage getStage() {
		return stage;
	}

}