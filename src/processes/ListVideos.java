package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Task that finds and lists all available videos in the file system
 */
public class ListVideos extends Task<Integer> {

    // Field declarations
    private ListView<String> videoList;
    private ArrayList<String> out;

    /**
     * Constructor for the task that finds and lists all available videos in the file system
     * @param videoList
     */
    public ListVideos(ListView<String> videoList) {
        this.videoList = videoList;
    }

    @Override
    protected Integer call() {
        try {
            listCreations();
            return out.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Runs the bash script that lists all available videos in the file system
     * @throws Exception
     */
    private ArrayList<String> listCreations() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/listVideos.sh");
        Process process = pb.start();

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            String line;
            ArrayList<String> outputList = new ArrayList<>();
            while ((line = stdout.readLine()) != null) {
                outputList.add(line);
            }
            out = outputList;
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
        return null;
    }

    /**
     * Update the GUI if necessary
     */
    @Override
    protected void done() {
        Platform.runLater(() -> {
        	try {
        		videoList.getItems().clear();
        		videoList.getItems().addAll(out);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        });
    }

}
