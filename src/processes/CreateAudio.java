package processes;

import javafx.concurrent.Task;

import java.util.ArrayList;

/**
 * Class that handles the task of creating one audio file by running a bash script
 */
public class CreateAudio extends Task<ArrayList<String>> {

    // Field declarations
    private String videoName;
    private String text;
    private String voice;

    /**
     * Constructor for the task that handles the creation of one audio file using input text, and a voice to speak this text
     * @param videoName
     * @param text
     * @param voice
     */
    public CreateAudio(String videoName, String text, String voice) {
        this.videoName = videoName;
        this.text = text;
        this.voice = voice;
    }

    /**
     * Runs when the task is started
     */
    @Override
    protected ArrayList<String> call() {
        try {
        	createAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an audio file from a voice and a piece of text obtained from the CreateAudio object
     * @throws Exception
     */
    private void createAudio() throws Exception {
        // Run bash script to create audio file from input
        ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/createAudio.sh", videoName, text, voice);
        Process process = pb.start();
        process.waitFor();
    }

}
