package app.controllers;

import java.io.File;
import java.util.ArrayList;
import app.*;
import app.controllers.helpers.ReviewControllerHelper;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * Controller class for the Review scene / Media player - is a draggable window
 */
public class ReviewController extends DraggableWindow {

	/***************************** FIELD DECLARATIONS ********************************/
	// Root of the scene
	@FXML private AnchorPane root;
	
	// StackPane for dialogs to show up in
	@FXML private StackPane stackPane;

	// Media player fields with buttons etc.
	@FXML private MediaView screen;
	@FXML private Label timeLabel;
	@FXML private JFXToggleButton toggleMusicButton;
	@FXML private JFXSlider timeSlider;
	@FXML private JFXListView playListView;
	@FXML private JFXTextArea transcript;
	@FXML private Label upcomingLabel;
	@FXML private JFXButton playButton;
	@FXML private JFXButton muteButton;

	// Icons
	@FXML private MaterialDesignIconView playIcon;
	@FXML private MaterialDesignIconView muteIcon;
	@FXML private MaterialDesignIconView favHeart;

	// List of background music
	@FXML private JFXComboBox<String> musicList;
	
	// Help "?" buttons/tooltips
	@FXML private JFXButton helpQuit;
	@FXML private JFXButton helpMusicList;
	@FXML private JFXButton helpMute;
	@FXML private JFXButton helpFor5;
	@FXML private JFXButton helpBack5;
	@FXML private JFXButton helpPlayPause;
	@FXML private JFXButton helpMusicToggle;
	@FXML private JFXButton helpNext;
	@FXML private JFXButton helpPrev;
	@FXML private JFXButton helpList;
	@FXML private JFXButton helpPlayButton;
	@FXML private JFXButton helpTextArea;
	@FXML private JFXButton helpBack;
	@FXML private JFXButton helpFavHeart;
	
    private ReviewControllerHelper helper = new ReviewControllerHelper();

	// Set up media player and playlist
	private MediaPlayer player;
	private MediaPlayer music;
	private ArrayList<VideoCreation> playList;
	private VideoCreation currentVideo;
	private int playIndex = 0;

	/***************************** FXML METHODS ********************************/

	/**
	 * Play or pause the video
	 */
	@FXML
	private void playPause() {
		if (player.getStatus() == Status.PLAYING) player.pause();
		else  player.play();
		helper.switchPlayIcon(player, playButton, playIcon);
	}


	/**
	 * Go back 5 seconds in the video being played
	 */
	@FXML
	private void back() {
		player.seek(player.getCurrentTime().add( Duration.seconds(-5)) );
	}

	/**
	 * Go forward 5 seconds in the video being played
	 */
	@FXML
	private void forward() {
		player.seek(player.getCurrentTime().add( Duration.seconds(5)) );
	}

	/**
	 * Mute the sound of the video or unmute
	 */
	@FXML
	private void mute() {
		helper.mute(player, muteButton, muteIcon);
	}

	/**
	 * Turn on/off background music
	 */
	@FXML
	private void toggleMusic() {
		// Turn music on if it's currently off, and off if it's currently on
		music.setMute(!toggleMusicButton.isSelected());
	}

	/**
	 * Play next video
	 */
	@FXML
	private void nextVideo() {
		if ((playIndex+1) == playList.size()) return;
		playIndex++;
		player.dispose();
		setSource();
	}

	/**
	 * Play previous video
	 */
	@FXML
	private void prevVideo() {
		if (playIndex == 0) return;
		playIndex--;
		player.dispose();
		setSource();
	}

	/**
	 * Play the video the user selected from the playlist
	 */
	@FXML
	private void playVideo() {
		playIndex = playListView.getSelectionModel().getSelectedIndex();
		player.dispose();
		setSource();
	}

	/**
	 * Show that the style of the icon is selected when user hovers over the icon
	 */
	@FXML
	private void favEnter() {
		favHeart.setIcon(MaterialDesignIcon.HEART);
	}

	/**
	 * Show that the style of the icon is the state of whether the video is a favourite or not when user stops hovering over the icon
	 */
	@FXML
	private void favExit() {
		if (currentVideo.getFavourite()) {
			favHeart.setIcon(MaterialDesignIcon.HEART);
		} else {
			favHeart.setIcon(MaterialDesignIcon.HEART_OUTLINE);
		}
	}

	/**
	 * Toggle the favourite status of the video when icon clicked
	 */
	@FXML
	private void favClick() {
		if (currentVideo.toggleFavourite()) {
			favHeart.setIcon(MaterialDesignIcon.HEART);
		} else {
			favHeart.setIcon(MaterialDesignIcon.HEART_OUTLINE);
		}
	}

	/**
	 * Quit back to the home page
	 */
	@FXML
	private void home() {
		// Stop playing any media
		player.dispose();
		music.dispose();
		// Switch to home page
		new WindowBuilder().switchScene("HomePage", "VarPedia", root.getScene());
	}

	/**
	 * Quit the application altogether
	 */
	@FXML
	private void quit() {
		timeLabel.getScene().getWindow().hide();
		VideoManager.getVideoManager().writeSerializedVideos();

		// Stop playing any media
		player.dispose();
		music.dispose();
	}

	/**
	 * Runs upon startup of the scene, sets up any nodes that need to be initialised
	 */
	@FXML
	private void initialize() {
		// Set up tooltips
		setUpHelp();
		// Set up drop-down selection box for background music
		helper.setUpMusicBox(musicList);
		musicList.setOnAction(e-> {
			// Update music playing
			music.dispose();
			updateSong();
		});
	}

	/***************************** HELPER METHODS ********************************/
	// These helper methods deal with objects that can't be passed into a helper class, and therefore must remain here, such as the methods that handle the currently playing video and music

	/**
	 * Create help tooltips
	 */
	private void setUpHelp() {
		helpMute.setTooltip(new HoverToolTip("Click this to mute the video's voice!").getToolTip());
		helpFor5.setTooltip(new HoverToolTip("Click this to go 5 seconds further into the video!").getToolTip());
		helpBack5.setTooltip(new HoverToolTip("Click this to go 5 seconds back in the video!").getToolTip());
		helpQuit.setTooltip(new HoverToolTip("Click this button to quit the application!").getToolTip());
		helpPlayPause.setTooltip(new HoverToolTip("Click this to play the video if it is paused, or pause the video if it is playing!").getToolTip());
		helpMusicToggle.setTooltip(new HoverToolTip("Click this to turn on some background music (or to turn it off if it is already playing!)").getToolTip());
		helpList.setTooltip(new HoverToolTip("This is where all the videos you can play are listed!").getToolTip());
		helpPlayButton.setTooltip(new HoverToolTip("After choosing a video from the list above by clicking on it, click this button to play that video!").getToolTip());
		helpNext.setTooltip(new HoverToolTip("Click this to play the next video in the list!").getToolTip());
		helpPrev.setTooltip(new HoverToolTip("Click this to play the previous video in the list!").getToolTip());
		helpTextArea.setTooltip(new HoverToolTip("This is where the text you selected for the video that is currently playing shows up so you can read along with the video!").getToolTip());
		helpBack.setTooltip(new HoverToolTip("Click this button to go back to the main menu!").getToolTip());
		helpMusicList.setTooltip(new HoverToolTip("To change the song playing, click this box and then click on the song you want to play in the background. \nMake sure to turn the music off and on again using the toggle button to the right to make the music change!").getToolTip());
		helpFavHeart.setTooltip(new HoverToolTip("Click this heart to add or remove the current video to or from your favourites!").getToolTip());
	}

	/**
	 * Populate the playlist with videos for a given list of videos
	 * @param playList
	 */
	void setPlaylist(ArrayList<VideoCreation> playList) {
		this.playList = playList;
		for (VideoCreation v: playList) playListView.getItems().add(v.getName());
		// Setup background music player
		updateSong();
		// Setup video player
		setSource();
	}
	
    /**
     * Get the song that the user selected from the music combo box and update the current song
     * @return the selected song
     */
	private Media updateSong() {
		String name = musicList.getSelectionModel().getSelectedItem();
		String realName = Music.findMusic(name);
		File fileUrl = new File("src/" + realName + ".wav");
		Media audio = new Media(fileUrl.toURI().toString());
		music = new MediaPlayer(audio);
		music.setAutoPlay(true);
		music.setMute(!toggleMusicButton.isSelected());
		music.setCycleCount(MediaPlayer.INDEFINITE);
		return audio;
	}

	/**
	 * Set a new video as playing, updating the video player elements
	 */
	private void setSource() {
		currentVideo = playList.get(playIndex);
		helper.resetControlButtons(helpPlayButton, playIcon, muteButton, muteIcon);
		helper.setUpStar(currentVideo, favHeart);
		helper.updatePlaylist(upcomingLabel, playList, playListView, playIndex);
		helper.updateTranscript(currentVideo, transcript);
		helper.slider(player, timeSlider, playButton, playIcon);
		helper.setUpStar(currentVideo, favHeart);
	}
	
	/**
	 * Set up the video player with a video
	 */
	public void setupPlayer() {
		// Setup video player with source file
		File fileUrl = new File("videos/" + currentVideo.getName() + ".mp4");
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		// Show the rating window when finished playing
		player.setOnEndOfMedia(()-> {
			currentVideo.incrementViews();
			helper.showRating(playList, playIndex, stackPane);
		});
		screen.setMediaPlayer(player);
		helper.updateTimeLabel(player, timeLabel);
	}

}
