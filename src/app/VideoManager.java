package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class VideoManager {
	private static VideoManager videoManager;
	private ArrayList<VideoCreation> videos = new ArrayList<VideoCreation>();
	
	public static VideoManager getVideoManager() {
		if (videoManager == null) videoManager = new VideoManager();
		return videoManager;
	}
	
	public void add(VideoCreation video) {
		videos.add(video);
    	System.out.println(videos);
	}
	
	public void delete(VideoCreation video) {
		videos.remove(video);
    	System.out.println(videos);
	}
	
    public void writeSerializedVideos() {
    	System.out.println("Writing serialization");
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("VideoCreations.bin"));
			objectOutputStream.writeObject(videos);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public ArrayList<VideoCreation> readSerializedVideos() {
    	System.out.println("Reading serialization");

		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("VideoCreations.bin"));			
			System.out.println("Stored in VideoCreations.bin:");
			videos= (ArrayList<VideoCreation>) objectInputStream.readObject();
			
			System.out.println("Videos now are: " + videos);
			return videos;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No videos are saved currently");
		}
		return videos;
    }
    
    public ArrayList<VideoCreation> getVideos() {
		return videos;
	}

}
