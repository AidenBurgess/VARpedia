package app;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SingleStage {
	
	private static SingleStage singleStage;
	private Stage stage = new Stage();
	
	// Singleton, ensures only one stage is present at a time
	public static SingleStage getSingleStage() {
		if (singleStage == null) {
			singleStage = new SingleStage();
			singleStage.stage.initStyle(StageStyle.TRANSPARENT);
		}
		return singleStage;
	}
	
	public Stage stage() {
		return stage;
	}

}
