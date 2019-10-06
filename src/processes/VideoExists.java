package processes;

import javafx.concurrent.Task;
import java.io.IOException;

public class VideoExists extends Task<Boolean> {

    private String videoName;

    public VideoExists(String videoName) {
        this.videoName = videoName;
    }

    @Override
    protected Boolean call() {
        return doesExist();

    }

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
