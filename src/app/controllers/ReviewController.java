package app.controllers;

import java.io.File;
import java.util.ArrayList;

import app.*;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
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

	// Set up media player and playlist
	private MediaPlayer player;
	private MediaPlayer music;
	private ArrayList<VideoCreation> playList;
	private VideoCreation currentVideo;
	private int playIndex = 0;

	/***************************** FXML METHODS ********************************/

	// Play or pause the video
	@FXML
	private void playPause() {
		// Pause
		if (player.getStatus() == Status.PLAYING) {
			player.pause();
			playButton.setText("Play");
			playIcon.setStyle("-glyph-name:PLAY");
			// Play
		} else {
			player.play();
			playButton.setText("Pause");
			playIcon.setStyle("-glyph-name:PAUSE");
		}
	}

	// Go back 5 seconds in the video being played
	@FXML
	private void back() {
		player.seek(player.getCurrentTime().add( Duration.seconds(-5)) );
	}

	// Go forward 5 seconds in the video being played
	@FXML
	private void forward() {
		player.seek(player.getCurrentTime().add( Duration.seconds(5)) );
	}

	// Mute the sound of the video or unmute
	@FXML
	private void mute() {
		player.setMute(!player.isMute());
		// Mute
		if (player.isMute()) {
			muteButton.setText("Unmute");
			muteIcon.setStyle("-glyph-name:VOLUME_HIGH");
			// Unmute
		} else {
			muteButton.setText("Mute");
			muteIcon.setStyle("-glyph-name:VOLUME_OFF");
		}
	}

	// Turn on/off background music
	@FXML
	private void toggleMusic() {
		music.setMute(!toggleMusicButton.isSelected());
	}

	// Play next video
	@FXML
	private void nextVideo() {
		if ((playIndex+1) == playList.size()) return;
		playIndex++;
		player.dispose();
		setSource();
	}

	// Play previous video
	@FXML
	private void prevVideo() {
		if (playIndex == 0) return;
		playIndex--;
		player.dispose();
		setSource();
	}

	// Play the video the user selected from the playlist
	@FXML
	private void playVideo() {
		playIndex = playListView.getSelectionModel().getSelectedIndex();
		player.dispose();
		setSource();
	}

	// Quit back to the home page
	@FXML
	private void home() {
		shutdown();
		new WindowBuilder().switchScene("HomePage", "VarPedia", root.getScene());
	}

	// Quit the application altogether
	@FXML
	private void quit() {
		timeLabel.getScene().getWindow().hide();
		VideoManager.getVideoManager().writeSerializedVideos();
		shutdown();
	}

	// Runs upon startup of the scene, sets up any nodes that need to be initialised
	@FXML
	private void initialize() {
		// Set up tooltips
		setUpHelp();
		// Set up drop-down selection box for background music
		setUpMusicSelection();
	}

	/***************************** HELPER METHODS ********************************/

	// Populate the drop-down selection box for music
	private void setUpMusicSelection() {
		ArrayList<String> musicChoices = new ArrayList<>();
		musicChoices.add(0,"Mattioli Prelude");
		musicChoices.add("Piano and Cello");
		musicChoices.add("Entre Les Murs");
		musicList.setItems(FXCollections.observableArrayList(musicChoices));
		musicList.getSelectionModel().select(0);
		musicList.setOnAction(e-> updateMusic());
	}

	// Create help tooltips
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
	}

	// Allows user to rate the videos as they watch them
	private void showRating() {
		WindowBuilder windowBuilder = new WindowBuilder().noTop("RatingPopup", "Rate the video!");
		// Pass in video being rated
		VideoCreation currentVideo = playList.get(playIndex);
		windowBuilder.stage().setOnHidden(e-> {
			Integer rating = ((RatingController) windowBuilder.controller()).getRating();
			if(rating != null) {
				currentVideo.setRating(rating);
			}
			new DialogBuilder().close(stackPane, "Well Done!", "You just reviewed " + currentVideo.getName() + "!");
		});
	}

	// Populate the playlist with videos
	public void setPlaylist(ArrayList<VideoCreation> playList) {
		this.playList = playList;
		for (VideoCreation v: playList) playListView.getItems().add(v.getName());
		// Setup background music player
		updateSong();
		music.setMute(true);
		// Setup video player
		setSource();
	}

	// Get the song that the user selected from the music combo box
	private Media updateSong() {
		String name = musicList.getSelectionModel().getSelectedItem();
		String realName = Music.findMusic(name);
		File fileUrl = new File("src/" + realName + ".wav");
		Media audio = new Media(fileUrl.toURI().toString());
		music = new MediaPlayer(audio);
		music.setAutoPlay(true);
		music.setCycleCount(MediaPlayer.INDEFINITE);
		return audio;
	}

	// Set a new video as playing
	private void setSource() {
		currentVideo = playList.get(playIndex);
		setupPlayer();
		updateSidePanel();
		slider();

		// Update stage title to video name
		Stage currentStage = (Stage) timeLabel.getScene().getWindow();
		currentStage.setTitle("Currently playing: " + currentVideo.getName());
	}

	// Set up the video player with a video
	private void setupPlayer() {
		// Setup video player with source file
		File fileUrl = new File("videos/" + currentVideo.getName() + ".mp4");
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		// Reset okay and mute buttons
		playButton.setText("Pause");
		playIcon.setStyle("-glyph-name:PAUSE");
		muteButton.setText("Mute");
		muteIcon.setStyle("-glyph-name:VOLUME_OFF");
		// Show the rating window when finished playing
		player.setOnEndOfMedia(()-> {
			currentVideo.incrementViews();
			showRating();
		});
		screen.setMediaPlayer(player);
		// Timer label tracks the time of the video
		player.currentTimeProperty().addListener((observable,oldValue,newValue) -> {
			String time = "";
			time += String.format("%02d", (int)newValue.toMinutes());
			time += ":";
			time += String.format("%02d", (int)newValue.toSeconds()%60);
			timeLabel.setText(time);
		});
	}

	// Update playlist, upcoming video, and transcript of current video
	private void updateSidePanel() {
		// Update upcoming label
		if ((playIndex+1) == playList.size()) upcomingLabel.setText("Upcoming: None." );
		else upcomingLabel.setText("Upcoming: " + playList.get(playIndex+1).getName());
		//
		playListView.getSelectionModel().select(playIndex);
		// Update transcript
		String transcriptString = "";
		for(String line: currentVideo.getTextContent()) {
			transcriptString += line.trim() + "\n";
		}
		transcript.setText(transcriptString);
	}

	// Time slider for videos
	private void slider() {
		// Providing functionality to time slider
		player.currentTimeProperty().addListener(ov -> updatesValues());

		// In order to jump to the certain part of video
		timeSlider.valueProperty().addListener(ov-> {
			if (timeSlider.isPressed()) {
				player.pause();
				double percent = timeSlider.getValue()/player.getTotalDuration().toSeconds();
				player.seek(player.getTotalDuration().multiply(percent));
			}
		});
		timeSlider.setOnMouseReleased(e-> player.play());
		timeSlider.setOnDragDropped(e-> player.play());

		// Custom indicator labeling
		timeSlider.setValueFactory(arg0-> {
			return Bindings.createStringBinding(()->{
				String time = "";
				time += String.format("%02d", (int)player.getCurrentTime().toMinutes());
				time += ":";
				time += String.format("%02d", (int)player.getCurrentTime().toSeconds()%60);
				return time;
			}, timeSlider.valueProperty());
		});
	}

	// Set the time on the time slider
	private void updatesValues() {
		Platform.runLater(()-> {
			timeSlider.setMax(player.getTotalDuration().toSeconds());
			timeSlider.setValue(player.getCurrentTime().toSeconds());
		});
	}
	
	private void updateMusic() {
		music.dispose();
		updateSong();
	}

	// Stop playing any media
	public void shutdown() {
		player.dispose();
		music.dispose();
	}

}
