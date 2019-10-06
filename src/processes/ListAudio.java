package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListAudio extends Task<Integer> {

    private ListView<String> audioList;
    private ArrayList<String> out;

    public ListAudio(ListView<String> audioList) {
        this.audioList = audioList;
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

    private ArrayList<String> listCreations() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/listAudio.sh");
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
            return outputList;
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
        return null;
    }

    @Override
    protected void done() {
        Platform.runLater(() -> {
        	try {
        		audioList.getItems().clear();
        		audioList.getItems().addAll(out);
        	} catch(Exception e) {
        		System.out.println("None present");
        	}
        });
    }

}
