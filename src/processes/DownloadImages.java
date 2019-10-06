package processes;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import javafx.concurrent.Task;
import java.util.ArrayList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;


public class DownloadImages extends Task<ArrayList<String>> {
	
	private int numImages;
	private String searchTerm;
	
	public DownloadImages(int numImages, String searchTerm) {
		this.numImages = numImages;
		this.searchTerm = searchTerm;
	}
	
	@Override
	protected ArrayList<String> call() {
        try {
            downloadImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public String getAPIKey(String key) throws Exception {
		
		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "build"
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt";
		
		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}
	
	public void downloadImages() {
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

			int resultsPerPage = Integer.valueOf(numImages);
			int page = 0;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(searchTerm);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        System.out.println("Retrieving " + results.size()+ " results");
	        
	        for (Photo photo: results) {
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.MEDIUM);	
		        	String filename = photo.getId() + ".jpg";
		        	File outputfile = new File("./images",filename);
		        	ImageIO.write(image, "jpg", outputfile);
		        	System.out.println("Downloaded "+filename);
	        	} catch (FlickrException fe) {
	        		System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
	        }
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
