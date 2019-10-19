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
            ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/checkExistence.sh", videoName);
            Process process = pb.start();
            int exitStatus = 0;
            exitStatus = process.waitFor();
            System.out.println(exitStatus);
            if (exitStatus == 0) {
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
