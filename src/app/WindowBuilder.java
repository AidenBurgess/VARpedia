package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

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
        
        stage = new SingleStage().stage();
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
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
    	scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.show();
    	((DraggableWindow) loader.getController()).makeStageDrageable();
        return this;
    }
    
    public WindowBuilder switchScene(String screenName, String title, Scene scene) {
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(screenName + ".fxml"));
        try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        scene.setRoot(loader.getRoot());
        scene.getWindow().sizeToScene();
    	((DraggableWindow) loader.getController()).makeStageDrageable();
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