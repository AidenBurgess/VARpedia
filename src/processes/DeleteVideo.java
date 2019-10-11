package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.util.ArrayList;

import app.DialogBuilder;

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
        // Run bash script to delete the input video file from it's directory
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/deleteVideo.sh", video);
        Process process = pb.start();

        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            System.out.println("Completed Successfully");
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
    }
}
