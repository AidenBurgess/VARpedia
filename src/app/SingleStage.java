package app;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class ensuring only one stage is created and present at a time
 */
public class SingleStage {

	// Field declarations
	private static SingleStage singleStage;
	private Stage stage = new Stage();

	/**
	 * Singleton, ensures only one stage is present at a time
	 * @return the current stage
	 */
	public static SingleStage getSingleStage() {
		if (singleStage == null) {
			singleStage = new SingleStage();
			singleStage.stage.initStyle(StageStyle.TRANSPARENT);
		}
		return singleStage;
	}

	/**
	 * @return the current stage
	 */
	public Stage stage() {
		return stage;
	}

}
