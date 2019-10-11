package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import app.DialogBuilder;

public class SearchWiki extends Task<ArrayList<String>> {

    private TextArea textArea;
    private String videoName;
    private StackPane stackPane;
    private ArrayList<String> out;

    public SearchWiki(String videoName, TextArea textArea, StackPane stackPane) {
        this.videoName = videoName;
        this.textArea = textArea;
        this.stackPane = stackPane;
    }

    @Override
    protected ArrayList<String> call() {
        try {
            return search();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> search() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/searchWiki.sh", videoName);
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
            Platform.runLater(() -> {
            	new DialogBuilder().close(stackPane, "Search error", videoName + " can not be found!");
            });
        }
        return null;
    }

    @Override
    protected void done() {
        Platform.runLater(() -> {
            textArea.setText("");
            if (out == null) {
                return;
            }
            for (String line : out) {
                textArea.appendText(line + "\n");
            }
        });
    }

}
