package app;

import app.controllers.HomeController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MAIN CLASS
* Run the application and start up the home page
*/
public class HomePage extends Application {

    /**
     * Start up the application by creating the scene and window
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
    	WindowBuilder windowBuilder = new WindowBuilder().noTop("HomePage", "VARpedia");
    	((HomeController) windowBuilder.controller()).remindReview();
    }

    /**
     * main method; the method that launches the application
     * @param args
     */
    public static void main(String[] args) {
    	launch(args);
    }

}
