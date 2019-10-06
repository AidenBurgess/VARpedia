package processes;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class PlayVideo extends Task<ArrayList<String>> {

    private String videoName;

    public PlayVideo(String videoName) {
        this.videoName = videoName;
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

        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/playVideo.sh", videoName);
        Process process = pb.start();

        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            System.out.println("Completed Successfully");
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
    }

}
