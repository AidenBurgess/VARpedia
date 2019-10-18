package app;

import app.controllers.HomeController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
* Run the application and start up the home page
*/
public class HomePage extends Application {
    @Override
    public void start(Stage stage) throws Exception {
    	WindowBuilder windowBuilder = new WindowBuilder().noTop("HomePage", "VARpedia");
    	((HomeController) windowBuilder.controller()).remindReview();
    }
    
    public static void main(String[] args) {
    	launch(args);
    }

}
