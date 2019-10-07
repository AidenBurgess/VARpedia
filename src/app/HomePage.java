package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomePage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("HomePage.fxml"));
        Parent layout = loader.load();
        Scene scene = new Scene(layout);
        stage.setOnCloseRequest(e-> {
        	VideoManager.getVideoManager().writeSerializedVideos();
        });
        
        stage.setTitle("VARpedia");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
    	launch(args);
    }

}
