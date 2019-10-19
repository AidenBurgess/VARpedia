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

    public WindowBuilder noTop(String screenName, String title) {
        try {
            loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("resources/" + screenName + ".fxml"));
            scene = null;
            scene = new Scene(loader.load());
            System.out.println(scene.getHeight());
            System.out.println(scene.getX());
            stage = new Stage();
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.show();
            ((DraggableWindow) loader.getController()).makeStageDrageable();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        /*loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("resources/" + screenName + ".fxml"));
        scene = null;
        System.out.println("What about here?");
        try {
            scene = new Scene(loader.load());
            System.out.println("Did we make it this far?");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(scene.getHeight());
        System.out.println(scene.getX());
        stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
    	scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.show();
    	((DraggableWindow) loader.getController()).makeStageDrageable();
        return this;*/
    }
    
    public WindowBuilder switchScene(String screenName, String title, Scene scene) {
        loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("resources/" + screenName + ".fxml"));
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