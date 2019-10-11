package app;

import javafx.application.Application;
import javafx.stage.Stage;

// Start up the home page
public class HomePage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
    	WindowBuilder windowBuilder = new WindowBuilder().noTop("NewHomePage", "VARpedia");
    	((HomeController) windowBuilder.controller()).makeStageDrageable();
    	((HomeController) windowBuilder.controller()).remindReview();
        stage.setOnCloseRequest(e-> {
        	VideoManager.getVideoManager().writeSerializedVideos();
        });
    }
    
    public static void main(String[] args) {
    	launch(args);
    }

}
