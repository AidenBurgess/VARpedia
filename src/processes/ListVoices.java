package processes;

import app.Voice;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Task that lists the voices available on the computer
 */
public class ListVoices extends Task<ArrayList<String>> {

    // Field declarations
    private ArrayList<String> out;

    /**
     * Runs when the task is started
     */
    @Override
    protected ArrayList<String> call() {
        try {
        	return listVoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Run the bash script that lists the computer's voices
     * @throws Exception
     */
    private ArrayList<String> listVoices() throws Exception {
        // Run the bash script required through a process
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/listVoices.sh");
        Process process = pb.start();

        // Set up a reader for the output of the process
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Don't do anything else until the process has finished
        int exitStatus = process.waitFor();

        // Output the list of voices obtained from the process
        if (exitStatus == 0) {
            String line;
            ArrayList<String> outputList = new ArrayList<>();
            int i = 1;
            while ((line = stdout.readLine()) != null) {
                Voice voice = new Voice(line, "Voice Option " + i);
                outputList.add(voice.toString());
                i++;
            }
            out = outputList;
        }

        return out;
    }

}
