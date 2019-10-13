package app;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SingleStage {
	
	private static SingleStage singleStage;
	private Stage stage = new Stage();
	
	public static SingleStage getSingleStage() {
		if (singleStage == null) {
			singleStage = new SingleStage();
			singleStage.stage.initStyle(StageStyle.TRANSPARENT);
//	    	scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
		}
		return singleStage;
	}
	
	public Stage stage() {
		return stage;
	}

}
