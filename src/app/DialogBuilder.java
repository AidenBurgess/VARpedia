package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
* Helper class used to create different types of dialogues/popups
*/
public class DialogBuilder {
	// Set up dialog
	private JFXDialog dialog;
	
	// Set up a dialog with a close button that closes it
	public void close(StackPane stackPane, String title, String body) {
		 JFXDialogLayout dialogContent = new JFXDialogLayout();
		// Set the dialogue's text
         dialogContent.setHeading(new Text(title));
         dialogContent.setBody(new Text(body));
		// Add close button
         JFXButton close = new JFXButton("Close");
         close.getStyleClass().add("JFXButton");
         dialogContent.setActions(close);
         dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.TOP);
         close.setOnAction( e -> dialog.close());
         dialog.show();
	}
	
	// Set up a loading dialog with a gif to show a process is taking place
	public JFXDialog loading(StackPane stackPane, String title) {
    	JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        JFXSpinner spinner = new JFXSpinner();
        spinner.setPrefSize(50, 50);
        dialogContent.setBody(spinner);
        dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.TOP);
        dialog.show();
        return dialog;
	}
	
	// Set up a dialog with a cancel button and a confirm button
	public JFXButton confirm(StackPane stackPane, String title, String body) {
		 JFXDialogLayout dialogContent = new JFXDialogLayout();
         dialogContent.setHeading(new Text(title));
         dialogContent.setBody(new Text(body));
         JFXButton cancel = new JFXButton("Cancel");
         cancel.getStyleClass().add("JFXButton");
         JFXButton confirm= new JFXButton("Confirm");
         confirm.getStyleClass().add("JFXButton");
         dialogContent.setActions(cancel, confirm);
         dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.TOP);
         cancel.setOnAction( e -> dialog.close());
         dialog.show();
         return confirm;
	}
	
	// Get the created dialog
	public JFXDialog dialog() {
		return dialog;
	}

}
