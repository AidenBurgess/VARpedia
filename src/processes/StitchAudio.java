package processes;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StitchAudio extends Task<ArrayList<String>> {

    private List<String> audioFiles;

    public StitchAudio(List<String> audioFiles) {
        this.audioFiles = audioFiles;
    }

    @Override
    protected ArrayList<String> call() {
        try {
        	stichVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void stichVideo() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/stitchAudio.sh", String.join(" ", audioFiles));
        Process process = pb.start();

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line = null;
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }        
        line = null;
        while ((line = stdError.readLine()) != null) {
            System.out.println(line);
        }
        return;
     
    }
}
