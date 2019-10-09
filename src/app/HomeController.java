package app;

import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import processes.*;
import java.util.ArrayList;
import java.util.Optional;

public class HomeController {

    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton reviewButton;
    @FXML
    private JFXButton quitButton;
    @FXML
    private TableView videoTable;
    @FXML
    private JFXButton helpTableView;
    @FXML
    private JFXButton helpDeleteButton;
    @FXML
    private JFXButton helpCreateButton;
    @FXML
    private JFXButton helpPlayButton;
    @FXML
    private JFXButton helpReviewButton;
    @FXML
    private JFXButton helpVarPedia;
    @FXML
    private JFXButton helpHelp;
    @FXML
    private JFXButton helpQuitButton;
    @FXML
    private Label numVideoLabel;
    @FXML
    private StackPane stackPane;
    
    private VideoManager videoManager = VideoManager.getVideoManager();

    @FXML
    private void createVideo() {
    	Stage homeStage = (Stage) helpCreateButton.getScene().getWindow();
    	homeStage.hide();

    	Stage creationStage = new WindowBuilder().pop("NewVideoCreation", "Create a Video!").stage();
    	creationStage.setOnCloseRequest(e -> {
    		updateVideoTable(false);
    		homeStage.show();
    	});
    }
    
    @FXML
    private void playVideo() {
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	String videoName = videoCreation.getName();
    	if(videoName == null) return;

    	WindowBuilder windowBuilder = new WindowBuilder().pop("VideoPlayer", "Video Player");
    	FXMLLoader loader = windowBuilder.loader();
    	((VideoPlayerController) windowBuilder.loader().getController()).setSource(videoName);
    	windowBuilder.stage().setOnCloseRequest(e -> ((VideoPlayerController) loader.getController()).shutdown());
    }
    
    @FXML
    private void deleteVideo() {
    	VideoCreation videoCreation = (VideoCreation) videoTable.getSelectionModel().getSelectedItem();
    	String videoName = videoCreation.getName();
    	if(videoName == null) return;
    	System.out.println(videoName);
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Process");
        alert.setHeaderText("Deletion Confirmation");
        alert.setContentText("Would you really like to delete " + videoName + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        
        Task<ArrayList<String>> task = new DeleteVideo(videoName);
        task.setOnSucceeded(event -> updateVideoTable(false));
        Thread thread = new Thread(task);
        thread.start();
        videoManager.delete(videoCreation);
    }

    @FXML
    private void reviewVideos() {
    	// Determine which videos to review
    	ArrayList<VideoCreation> toReview;
    	toReview = new ArrayList(videoTable.getItems());
    	// Launch review window
    	WindowBuilder reviewWindow = new WindowBuilder().pop("ReviewPlayer", "Review Videos");
    	ReviewController controller = reviewWindow.loader().getController();
    	
    	controller.setPlaylist(toReview);
    	reviewWindow.stage().setOnHidden(e -> {
    		controller.shutdown();
    		updateVideoTable(false);
    	});
    }

    @FXML
    private void quit() {
    	quitButton.getScene().getWindow().hide();
    }

    private void updateVideoTable(boolean startup) {
    	videoTable.getItems().clear();
    	videoTable.getItems().addAll(videoManager.getVideos());
    }
    
    @FXML
    private void initialize() {
        initTable();
        setUpHelp();
    }
        
    private void initTable() {
        TableColumn<VideoCreation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(70);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));        

        TableColumn<VideoCreation, String> searchTermColumn = new TableColumn<>("Search Term");
        searchTermColumn.setMinWidth(80);
        searchTermColumn.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
        
        TableColumn<VideoCreation, String> numImagesColumn = new TableColumn<>("#Images");
        numImagesColumn.setMinWidth(80);
        numImagesColumn.setCellValueFactory(new PropertyValueFactory<>("numImages"));
        
        TableColumn<VideoCreation, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setMinWidth(80);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        videoTable.getItems().addAll(videoManager.readSerializedVideos());
        videoTable.getColumns().addAll(nameColumn, searchTermColumn, numImagesColumn, ratingColumn);
    }

    private void setUpHelp() {
        Tooltip tabView = new Tooltip("All of your video creations are listed here! \nClick on a row to select that video.");
        tabView.setWidth(200.0);
        tabView.setWrapText(true);
        helpTableView.setTooltip(tabView);

        Tooltip crte = new Tooltip("");
        crte.setWidth(200.0);
        crte.setWrapText(true);
        helpCreateButton.setTooltip(crte);

        Tooltip dlte = new Tooltip("");
        dlte.setWidth(200.0);
        dlte.setWrapText(true);
        helpDeleteButton.setTooltip(dlte);

        Tooltip help = new Tooltip("This is what the hover text will look like!");
        help.setWidth(200.0);
        help.setWrapText(true);
        helpHelp.setTooltip(help);

        Tooltip play = new Tooltip("");
        play.setWidth(200.0);
        play.setWrapText(true);
        helpPlayButton.setTooltip(play);

        Tooltip quit = new Tooltip("");
        quit.setWidth(200.0);
        quit.setWrapText(true);
        helpQuitButton.setTooltip(quit);

        Tooltip revB = new Tooltip("");
        revB.setWidth(200.0);
        revB.setWrapText(true);
        helpReviewButton.setTooltip(revB);

        Tooltip var = new Tooltip("");
        var.setWidth(200.0);
        var.setWrapText(true);
        helpVarPedia.setTooltip(var);
    }

    private int countWords(String input) {
        if (input == null || input.isEmpty()) {
          return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
      }

    private JFXDialog loadingDialog(String title) {
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
