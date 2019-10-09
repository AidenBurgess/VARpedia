package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class WindowBuilder {
	
	private FXMLLoader loader;
	private Stage stage;
	private Scene scene;

    public WindowBuilder pop(String screenName, String title) {
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(screenName + ".fxml"));
        scene = null;
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
    
    public WindowBuilder noTop(String screenName, String title) {
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(screenName + ".fxml"));
        scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
//    	scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.show();
        return this;
    }
    
    public FXMLLoader loader() {
    	return loader;
    }
    
    public Stage stage() {
		return stage;
	}
    
    public Scene scene() {
		return scene;
	}
    
    public Object controller() {
    	return loader.getController();
    }

}