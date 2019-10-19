package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import app.DialogBuilder;

/**
 * Task that takes a search term and retrieves the wikipedia definition for that term
 */
public class SearchWiki extends Task<ArrayList<String>> {

    // Field declarations
    private TextArea textArea;
    private String searchTerm;
    private StackPane stackPane;
    private ArrayList<String> out;

    /**
     * Constructor for the task that retrieves a wikipedia definition of a word, for it to be displayed on the textArea
     * @param searchTerm
     * @param textArea
     * @param stackPane
     */
    public SearchWiki(String searchTerm, TextArea textArea, StackPane stackPane) {
        this.searchTerm = searchTerm;
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

    /**
     * Actually search the wiki using a bash script
     * @throws Exception
     */
    private ArrayList<String> search() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/searchWiki.sh", searchTerm);
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
            	new DialogBuilder().close(stackPane, "Search error", searchTerm + " can not be found!");
            });
        }
        return null;
    }

    /**
     * Update the GUI with the output of the wiki search - printing the definition found on it
     */
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
