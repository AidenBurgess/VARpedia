package app;

import java.io.File;

import com.jfoenix.controls.JFXToggleButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class VideoPlayerController {
	
	@FXML
	private MediaView screen;
	@FXML
	private Label timeLabel;
	@FXML
	private JFXToggleButton toggleButton;
	
	private MediaPlayer player;
	private MediaPlayer music;
	
	public void setSource(String source) {
		// Setup video player with source file
		File fileUrl = new File("videos/" + source + ".mp4"); 
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		screen.setMediaPlayer(player);
		
		// Setup background musci player
	    fileUrl = new File("backgroundMusic" + ".wav");
	    Media audio = new Media(fileUrl.toURI().toString());
	    music = new MediaPlayer(audio);
	    music.setAutoPlay(true);
	    music.setMute(true);
	    music.setCycleCount(MediaPlayer.INDEFINITE);
		
	    // Timer label tracks the time of the video
		player.currentTimeProperty().addListener((observable,oldValue,newValue) -> {
				String time = "";
				time += String.format("%02d", (int)newValue.toMinutes());
				time += ":";
				time += String.format("%02d", (int)newValue.toSeconds()%60);
				timeLabel.setText(time);
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
		player.seek( player.getCurrentTime().add( Duration.seconds(-5)) );
	}
	
	@FXML
    private void forward() {
		player.seek( player.getCurrentTime().add( Duration.seconds(5)) );
	}
	
	@FXML
	private void mute() {
		player.setMute( !player.isMute() );
	}
	
	@FXML
	private void toggleMusic() {
		music.setMute(!toggleButton.isSelected());
	}
	
	@FXML
	private void quit() {
    	timeLabel.getScene().getWindow().hide();
    	shutdown();
	}

	public void shutdown() {
		player.dispose();
	}

}
