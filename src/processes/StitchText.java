package processes;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StitchText extends Task<ArrayList<String>> {

    private List<String> textPieces;

    public StitchText(List<String> textPieces) {
        this.textPieces = textPieces;
    }

    @Override
    protected ArrayList<String> call() {
        try {
        	combineText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void combineText() {
        String output = "";
        for (String s : this.textPieces) {
            output += " ";
            output += s;
        }

        /* ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/stitchAudio.sh", String.join(" ", audioFiles));
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
        return;*/
    }
}
