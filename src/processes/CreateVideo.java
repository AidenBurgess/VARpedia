package processes;

import javafx.concurrent.Task;
import java.util.ArrayList;

public class CreateVideo extends Task<ArrayList<String>> {

    private String searchTerm;
    private String numImages;
    private String videoName;

    public CreateVideo(String searchTerm, String numImages, String videoName) {
        this.searchTerm = searchTerm;
        this.numImages = numImages;
        this.videoName = videoName;
    }	
    	
    @Override
    protected ArrayList<String> call() {
        try {
            createVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
