package processes;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class PreviewAudio extends Task<ArrayList<String>> {

    private String text2Say;
    private String voice;


    public PreviewAudio(String text2Say, String voice) {
        this.text2Say = text2Say;
        this.voice = voice;
    }
    @Override
    protected ArrayList<String> call() {
        try {
            speak();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void speak() throws Exception {

        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/previewAudio.sh", text2Say, voice);
        Process process = pb.start();

        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            System.out.println("Completed Successfully");
        } else {
            System.out.println("Error code: " + exitStatus + " occurred");
        }
    }

}
