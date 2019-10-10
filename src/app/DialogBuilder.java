package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogBuilder {
	
	public void closeDialog(StackPane stackPane, String title, String body) {
		 JFXDialogLayout dialogContent = new JFXDialogLayout();
         dialogContent.setHeading(new Text(title));
         dialogContent.setBody(new Text(body));
         JFXButton close = new JFXButton("Close");
         close.getStyleClass().add("JFXButton");
         dialogContent.setActions(close);
         JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
         close.setOnAction( e -> dialog.close());
         dialog.show();
	}
	
	public JFXDialog loadingDialog(StackPane stackPane, String title) {
    	JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        JFXSpinner spinner = new JFXSpinner();
        spinner.setPrefSize(50, 50);
        dialogContent.setBody(spinner);
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.RIGHT);
        dialog.show();
        return dialog;
	}

}
