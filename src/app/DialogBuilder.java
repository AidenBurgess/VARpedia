package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;

import javafx.scene.control.Button;
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
         Text bodyText = new Text(body);
         bodyText.getStyleClass().add("closeDialogText");
         dialogContent.setBody(bodyText);
		// Add close button
         JFXButton close = new JFXButton("Close");
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
         JFXButton confirm= new JFXButton("Confirm");
         dialogContent.setActions(cancel, confirm);
         dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.TOP);
         cancel.setOnAction( e -> dialog.close());
         dialog.show();
         return confirm;
	}
	
	// Set up a dialog with a close and review buttons.
	public Button reminder(StackPane stackPane, String title, String body) {
		 JFXDialogLayout dialogContent = new JFXDialogLayout();
		// Set the dialogue's text
         dialogContent.setHeading(new Text(title));
         Text bodyText = new Text(body);
         bodyText.getStyleClass().add("closeDialogText");
         dialogContent.setBody(bodyText);
		// Add close button
         JFXButton close = new JFXButton("Close");
         JFXButton review = new JFXButton("Review");
         dialogContent.setActions(close, review);
         dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.TOP);
         close.setOnAction( e -> dialog.close());
         dialog.show();
         return review;
	}
	
	// Get the created dialog
	public JFXDialog dialog() {
		return dialog;
	}

}
