package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Class that keeps track of the current available videos.
 * Communicates details about videos and what happens to them between the file system and the application GUI
 */
public class VideoManager {
	// Field declarations
	private static VideoManager videoManager;
	private ArrayList<VideoCreation> videos;

	/**
	 * @return the video manager
	 */
	public static VideoManager getVideoManager() {
		if (videoManager == null) videoManager = new VideoManager();
		return videoManager;
	}

	/**
	 * Add the given video to the list of existing videos
	 * @param video
	 */
	public void add(VideoCreation video) {
		videos.add(video);
	}

	/**
	 * Delete the given video from the list of existing videos
	 * @param video
	 */
	public void delete(VideoCreation video) {
		videos.remove(video);
	}

	/**
	 * Update the state of what videos are available and the different videos' contents
	 */
    public void writeSerializedVideos() {
    	// Information on the videos is written to a bin file that stores the serialized information
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("VideoCreations.bin"));
			objectOutputStream.writeObject(videos);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * Find what videos are available and the contents of these videos
	 * @return a list of videos
	 */
	private ArrayList<VideoCreation> readSerializedVideos() {
		// If the variable videos is empty, ensure it contains an araylist to add information to
		if (videos == null) videos = new ArrayList<>();

		// Information on the videos retrieved from a bin file that stores the serialized information
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("VideoCreations.bin"));
			videos= (ArrayList<VideoCreation>) objectInputStream.readObject();
			return videos;
		} catch (IOException | ClassNotFoundException e) {

		}
		return videos;
    }

	/**
	 * @return the currently existing videos
	 */
	public ArrayList<VideoCreation> getVideos() {
    	if (videos == null) {
    		readSerializedVideos();
    	}
		return videos;
	}

}
