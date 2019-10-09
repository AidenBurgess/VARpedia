package app;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VideoCreation implements Serializable {
	private String name;
	private String searchTerm;
	private int numImages;
	private int rating;
	private int views;
	
	public VideoCreation(String name, String searchTerm, int numImages) {
		this.name = name;
		this.searchTerm = searchTerm;
		this.numImages = numImages;
		this.rating = 0;
		this.views = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getNumImages() {
		return numImages;
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}
	
	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
	
	public void incrementViews() {
		this.views++;
	}
	
	@Override
	public String toString() {
		return "VideoCreation [name=" + name + ", searchTerm=" + searchTerm + ", numImages=" + numImages + ", rating="
				+ rating + "]";
	}
	
}
