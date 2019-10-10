package app;

import java.io.File;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class ReviewController {
	
	@FXML
	private AnchorPane root;
	@FXML
	private MediaView screen;
	@FXML
	private Label timeLabel;
	@FXML
	private JFXToggleButton toggleMusicButton;
	@FXML
	private JFXSlider timeSlider;
	@FXML
	private JFXButton helpQuit;
	@FXML
	private JFXButton helpMute;
	@FXML
	private JFXButton helpFor5;
	@FXML
	private JFXButton helpBack5;
	@FXML
	private JFXButton helpPlayPause;
	@FXML
	private JFXButton helpMusicToggle;
	@FXML
	private JFXButton helpNext;
	@FXML
	private JFXButton helpPrev;
	@FXML
	private JFXButton helpList;
	@FXML
	private JFXButton helpPlaylist;

	private MediaPlayer player;
	private MediaPlayer music;
	private ArrayList<VideoCreation> playList;
	private int playIndex = 0;

	public void setPlaylist(ArrayList<VideoCreation> playList) {
//		root.setBackground(Background.EMPTY); 
		this.playList = playList;
		// Setup background music player
		File fileUrl = new File("backgroundMusic" + ".wav");
		Media audio = new Media(fileUrl.toURI().toString());
		music = new MediaPlayer(audio);
		music.setAutoPlay(true);
		music.setMute(true);
		music.setCycleCount(MediaPlayer.INDEFINITE);
		// Setup video player
		setSource();
	}
	
	private void setSource() {
		// Setup video player with source file
		File fileUrl = new File("videos/" + playList.get(playIndex).getName() + ".mp4"); 
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		player.setOnEndOfMedia(()-> showRating());
		screen.setMediaPlayer(player);
		slider();
		
		// Update stage title to video name
		Stage currentStage = (Stage) timeLabel.getScene().getWindow();
		currentStage.setTitle("Currently playing: " + playList.get(playIndex).getName());

		// Timer label tracks the time of the video
		player.currentTimeProperty().addListener((observable,oldValue,newValue) -> {
			String time = "";
			time += String.format("%02d", (int)newValue.toMinutes());
			time += ":";
			time += String.format("%02d", (int)newValue.toSeconds()%60);
			timeLabel.setText(time);
		});
	}

	private void slider() {
		// Providing functionality to time slider 
		player.currentTimeProperty().addListener(ov -> updatesValues());

		// Inorder to jump to the certain part of video 
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

	private void updatesValues() { 
		Platform.runLater(()-> {
			timeSlider.setMax(player.getTotalDuration().toSeconds());
			timeSlider.setValue(player.getCurrentTime().toSeconds());
		}); 
	} 

	@FXML
	private void playPause() {
		if (player.getStatus() == Status.PLAYING) {
			player.pause();
		} else {
			player.play();
		}
	}

	@FXML
	private void back() {
		player.seek(player.getCurrentTime().add( Duration.seconds(-5)) );
	}

	@FXML
	private void forward() {
		player.seek(player.getCurrentTime().add( Duration.seconds(5)) );
	}

	@FXML
	private void mute() {
		player.setMute(!player.isMute() );
	}

	@FXML
	private void toggleMusic() {
		music.setMute(!toggleMusicButton.isSelected());
	}
	
    @FXML
    private void nextVideo() {
    	if ((playIndex+1) == playList.size()) return;
    	playIndex++;
    	player.dispose();
    	setSource();
	}
    
    @FXML
    private void prevVideo() {
    	if (playIndex == 0) return;
    	playIndex--;
    	player.dispose();
    	setSource();
	}

	@FXML
	private void quit() {
		timeLabel.getScene().getWindow().hide();
		shutdown();
	}
	
	@FXML
	private void initialize() {
////		Scene scene = timeLabel.getScene();
////		Stage currentStage = (Stage) timeLabel.getScene().getWindow();
////		currentStage.initStyle(StageStyle.TRANSPARENT);
////		scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
	}

	private void setUpHelp() {
		helpMute.setTooltip(new HoverToolTip("Click this to mute the video's voice!").getToolTip());

		helpFor5.setTooltip(new HoverToolTip("Click this to go 5 seconds further into the video!").getToolTip());

		helpBack5.setTooltip(new HoverToolTip("Click this to go 5 seconds back in the video!").getToolTip());

		helpQuit.setTooltip(new HoverToolTip("Click this button to go back to the main menu!").getToolTip());

		helpPlayPause.setTooltip(new HoverToolTip("Click this to play the video if it is paused, or pause the video if it is playing!").getToolTip());

		helpMusicToggle.setTooltip(new HoverToolTip("Click this to turn on some background music (or to turn it off if it is already playing!)").getToolTip());

		helpList.setTooltip(new HoverToolTip("This is where all the videos you can play are listed!").getToolTip());

		helpPlaylist.setTooltip(new HoverToolTip("").getToolTip());

		helpNext.setTooltip(new HoverToolTip("Click this to play the next video in the list!").getToolTip());

		helpPrev.setTooltip(new HoverToolTip("Click this to play the previous video in the list!").getToolTip());
	}

	private void showRating() {
		WindowBuilder windowBuilder = new WindowBuilder().noTop("RatingPopup", "Rate the video!");
		// Pass in video being rated
		VideoCreation currentVideo = playList.get(playIndex);
		((RatingController) windowBuilder.controller()).setVideo(currentVideo);
		windowBuilder.stage().setOnHidden(e-> {
			Integer rating = ((RatingController) windowBuilder.controller()).getRating();
			if(rating != null) {
				currentVideo.setRating(rating);
				System.out.println(VideoManager.getVideoManager().getVideos());
			}
		});
	}

	public void shutdown() {
		player.dispose();
		music.dispose();
	}

}
