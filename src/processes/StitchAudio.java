package processes;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Task that combines multiple audio files to create one audio file
 */
public class StitchAudio extends Task<ArrayList<String>> {

    // Field declarations
    private List<String> audioFiles;

    /**
     * Constructor for the task that combines multiple audio files to create one audio file
     * @param audioFiles
     */
    public StitchAudio(List<String> audioFiles) {
        this.audioFiles = audioFiles;
    }

    @Override
    protected ArrayList<String> call() {
        try {
        	stitchAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actuallu combine the audio files using a bash script
     * @throws Exception
     */
    private void stitchAudio() throws Exception {
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
