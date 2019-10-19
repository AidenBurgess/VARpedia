package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class VideoManager {
	private static VideoManager videoManager;
	private ArrayList<VideoCreation> videos;
	
	public static VideoManager getVideoManager() {
		if (videoManager == null) videoManager = new VideoManager();
		return videoManager;
	}
	
	public void add(VideoCreation video) {
		videos.add(video);
	}
	
	public void delete(VideoCreation video) {
		videos.remove(video);
	}
	
    public void writeSerializedVideos() {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("VideoCreations.bin"));
			objectOutputStream.writeObject(videos);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private ArrayList<VideoCreation> readSerializedVideos() {
		if (videos == null) videos = new ArrayList<>();

		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("VideoCreations.bin"));
			videos= (ArrayList<VideoCreation>) objectInputStream.readObject();
			return videos;
		} catch (IOException | ClassNotFoundException e) {

		}
		return videos;
    }
    
    public ArrayList<VideoCreation> getVideos() {
    	if (videos == null) {
    		readSerializedVideos();
    	}
		return videos;
	}

}
