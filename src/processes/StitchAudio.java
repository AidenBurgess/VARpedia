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

    /**
     * Runs when the task is started
     */
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
     * Combine specified audio files in the file system using a bash script
     * @throws Exception
     */
    private void stitchAudio() throws Exception {
        // Run the bash script required through a process
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/stitchAudio.sh", String.join(" ", audioFiles));
        pb.start();     
    }
}
