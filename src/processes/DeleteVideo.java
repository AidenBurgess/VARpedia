package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class DeleteVideo extends Task<ArrayList<String>> {

    private String video;

    public DeleteVideo(String video) {
        this.video = video;
    }

    @Override
    protected ArrayList<String> call() {
        try {
            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void delete() throws Exception {

        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/deleteVideo.sh", video);
        Process process = pb.start();

        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            System.out.println("Completed Successfully");
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
    }

    @Override
    protected void done() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Process");
            alert.setHeaderText("Deletion Success");
            alert.setContentText(video + " has been deleted!");
            alert.showAndWait();
        });

    }

}
