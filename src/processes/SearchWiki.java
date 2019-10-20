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

    /**
     * Runs when the task is started
     */
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
     * Actually search the wiki for the term specified in the SearchWiki object, using a bash script
     * @throws Exception
     */
    private ArrayList<String> search() throws Exception {
        //Run the bash script required through a process
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/searchWiki.sh", searchTerm);
        Process process = pb.start();

        // Set up a reader for the output of the process
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Don't do anything until the process has finished
        int exitStatus = process.waitFor();

        if (exitStatus == 0) {
            // If the process finishes successfully, return the text output from the wiki search
            String line;
            ArrayList<String> outputList = new ArrayList<>();
            while ((line = stdout.readLine()) != null) {
                outputList.add(line);
            }
            out = outputList;
            return outputList;
        } else {
            // if the process wasn't successful, make sure a popup appears notifying the user of this
            Platform.runLater(() -> {
            	new DialogBuilder().close(stackPane, "Search error", "Whoops! " + searchTerm + " can not be found!");
            });
        }

        return null;
    }

    /**
     * Update the GUI with the output of the wiki search - printing the definition found on it
     * Done after the task has finished
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
