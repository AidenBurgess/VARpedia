package processes;

import javafx.concurrent.Task;
import java.util.ArrayList;

/**
 * Class that handles the creation of the video, specifically the image slideshow and combining this with the audio files
 */
public class CreateVideo extends Task<ArrayList<String>> {
    // Field declarations
    private String searchTerm;
    private String numImages;
    private String videoName;

    /**
     * Constructor for the task that handles the creation of videos - including an image slideshow and combining this with the audio files
     * @param searchTerm
     * @param numImages
     * @param videoName
     */
    public CreateVideo(String searchTerm, String numImages, String videoName) {
        this.searchTerm = searchTerm;
        this.numImages = numImages;
        this.videoName = videoName;
    }

    /**
     * Runs when the task is started
     */
    @Override
    protected ArrayList<String> call() {
        try {
            createVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates the final video creation from previously made audio files, and an image slideshow that is made in this method.
     * This runs a task that downloads images from Flickr, then a bash script to create the final video creation.
     * @throws Exception
     */
    private void createVideo() throws Exception {
        // Run bash script to make an images directory that stores the image files if it doesn't already exist
    	ProcessBuilder p = new ProcessBuilder().command("bash", "src/scripts/makeImages.sh");
    	p.start();
    	
    	// Download images off of Flickr
    	Task<ArrayList<String>> createVideo = new DownloadImages(Integer.valueOf(numImages), searchTerm);
    	Thread create = new Thread(createVideo);
    	create.start();
    	create.join();

    	// Run bash script to create the final video and save it as an mp4 file
    	ProcessBuilder pb = new ProcessBuilder().command("bash", "src/scripts/createVideo.sh", searchTerm, videoName, numImages);
    	pb.start();
    }

}
