package processes;

import app.Voice;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListVoices extends Task<ArrayList<String>> {

    private ArrayList<String> out;

    @Override
    protected ArrayList<String> call() {
        try {
        	return listVoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> listVoices() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/listVoices.sh");
        Process process = pb.start();

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

        int exitStatus = process.waitFor();
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
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
        return out;
    }

}
