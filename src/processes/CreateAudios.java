package processes;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.util.ArrayList;

public class CreateAudios extends Task<ArrayList<String>> {

	private ObservableList<String> audioText;
	private String voice;

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
        System.out.println(indiceStrings);
        return indiceStrings;
    }

}
