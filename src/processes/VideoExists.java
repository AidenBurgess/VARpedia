package processes;

import javafx.concurrent.Task;
import java.io.IOException;

/**
 * Task that checks if a video already exists in the file system
 */
public class VideoExists extends Task<Boolean> {

    // Field declarations
    private String videoName;

    /**
     * Constructor for the task that checks if a video by the name videoName already exists
     * @param videoName
     */
    public VideoExists(String videoName) {
        this.videoName = videoName;
    }

    /**
     * Runs when the task is started
     */
    @Override
    protected Boolean call() {
        return doesExist();

    }

    /**
     * Checks if the video corresponding to this object exists in the file system using a bash script
     * @return
     */
    private boolean doesExist() {
        try {
            // Run the bash script required through a process
            ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/checkExistence.sh", videoName);
            Process process = pb.start();

            // Don't do anything else until the process has finished
            int exitStatus = process.waitFor();

            // If the video exists, print true, else print false
            if (exitStatus == 0) {
                return false;
            } else {
                return true;
            }

            // Exception handling
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
