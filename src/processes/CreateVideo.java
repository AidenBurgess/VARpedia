
package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import java.util.ArrayList;

public class CreateVideo extends Task<ArrayList<String>> {

    private String searchTerm;
    private String numImages;
    private String videoName;

    public CreateVideo(String searchTerm, String numImages, String videoName) {
        this.searchTerm = searchTerm;
        this.numImages = numImages;
        this.videoName = videoName;
    }	
    	
    @Override
    protected ArrayList<String> call() {
        try {
            createVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createVideo() throws Exception {
    	ProcessBuilder p = new ProcessBuilder().command("bash", "src/scripts/makeImages.sh");
    	Process pro = p.start();
    	
    	int exit = pro.waitFor();
    	
    	Task<ArrayList<String>> createVideo = new DownloadImages(Integer.valueOf(numImages), searchTerm);
    	Thread create = new Thread(createVideo);
    	create.start();
    	create.join();
    	
    	ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/createVideo.sh", searchTerm, videoName, numImages);
    	Process process = pb.start();
    	
        int exitStatus = process.waitFor();
    }


    @Override
    protected void done() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Creation Process");
            alert.setHeaderText("Creation Success");
            alert.setContentText("Video has been created!");
            alert.showAndWait();
        });
    }

}
