package processes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class CreateAudio extends Task<ArrayList<String>> {

    private String videoName;
    private String text;
    private String voice;

    public CreateAudio(String videoName, String text, String voice) {
        this.videoName = videoName;
        this.text = text;
        this.voice = voice;
    }

    @Override
    protected ArrayList<String> call() {
        try {
        	createAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createAudio() throws Exception {
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/createAudio.sh", videoName, text, voice);
        Process process = pb.start();
        process.waitFor();
    }


    @Override
    protected void done() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Creation Process");
            alert.setHeaderText("Creation Success");
            alert.setContentText("Audio has been created!");
            alert.showAndWait();
        });
    }

}
