package processes;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.util.ArrayList;

/**
 * Class that handles the task of creating multiple audio files from the video's selected text
 */
public class CreateAudios extends Task<ArrayList<String>> {

    // Field declarations
	private ObservableList<String> audioText;
	private String voice;

    /**
     * Constructor for the task that handles the creation of several audio files representing the text of the video creation
     * @param audioText
     * @param voice
     */
    public CreateAudios(ObservableList<String> audioText, String voice) {
    	this.audioText = audioText;
    	this.voice = voice;
    }

    @Override
    protected ArrayList<String> call() {
        try {
        	return createAudios();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> createAudios() throws Exception {
    	ArrayList<String> indiceStrings = new ArrayList<String>();
    	int i = 0;
    	// Create an audio file for every piece of text selected by the user
        for (String audio: audioText) {
        	Thread thread = new Thread(new CreateAudio(Integer.toString(i), audio, voice));
        	thread.start();
        	thread.join();
        	indiceStrings.add(Integer.toString(i));
        	i++;
        }
        return indiceStrings;
    }

}
