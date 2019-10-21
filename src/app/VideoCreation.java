package app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that handles the serialization of videos.
 * Contains all the information of each video creation, including: it's name, search term, rating, view count, the number of images, and the text in the video
 */
@SuppressWarnings("serial")
public class VideoCreation implements Serializable {

	//Field declarations
	private String name;
	private String searchTerm;
	private int numImages;
	private int rating;
	private int views;
	private Boolean favourite;
	private ArrayList<String> textContent;

	/**
	 * Constructor for video objects, containing information on the video's name, search term, the number of images, the rating, the number of views, and the text content of the video.
	 * @param name
	 * @param searchTerm
	 * @param numImages
	 * @param textContent
	 */
	public VideoCreation(String name, String searchTerm, int numImages, ArrayList<String> textContent) {
		this.favourite = false;
		this.name = name;
		this.searchTerm = searchTerm;
		this.numImages = numImages;
		this.rating = 0;
		this.views = 0;
		this.textContent = textContent;
	}

	/**
	 * @return The name of the video
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The rating of the video
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * Set the rating of the video
	 * @param rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the number of images in the video
	 */
	public int getNumImages() {
		return numImages;
	}

	/**
	 * @return the search term used to create the video
	 */
	public String getSearchTerm() {
		return searchTerm;
	}

	/**
	 * @return the number of times the video has been viewed
	 */
	public int getViews() {
		return views;
	}

	/**
	 * Set the number of times the video has been viewed
	 * @param views
	 */
	public void setViews(int views) {
		this.views = views;
	}

	/**
	 * Increase the number of times the video has been viewed by one
	 */
	public void incrementViews() {
		this.views++;
	}

	public void setFavourite(Boolean fav) {
		this.favourite = fav;
	}

	public Boolean getFavourite() {
		return this.favourite;
	}

	/**
	 * @return the text content of the video
	 */
	public ArrayList<String> getTextContent() {
		return textContent;
	}

	/**
	 * @return a string representation of the contents of the video
	 */
	@Override
	public String toString() {
		return "VideoCreation [name=" + name + ", searchTerm=" + searchTerm + ", numImages=" + numImages + ", rating="
				+ rating + ", is favourite=" + favourite + "]";
	}
	
}
