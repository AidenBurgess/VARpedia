package processes;

import javafx.concurrent.Task;
import java.util.ArrayList;

/**
 * Task that handles the deletion of a video creation from the file system
 */
public class DeleteVideo extends Task<ArrayList<String>> {

    // Field declarations
    private String video;

    /**
     * Constructor for the task that handles the deletion of a video creation from the file system
     * @param video
     */
    public DeleteVideo(String video) {
        this.video = video;
    }

    /**
     * Runs when the task is started
     */
    @Override
    protected ArrayList<String> call() {
        try {
            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uses a bash script to delete the specified video in the DeleteVideo task object from the file system.
     * @throws Exception
     */
    private void delete() throws Exception {
        // Run bash script to delete the input video file from it's directory
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/deleteVideo.sh", video);
        pb.start();
    }
}
