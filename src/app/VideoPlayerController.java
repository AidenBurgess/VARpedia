package app;

import java.io.File;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
	private JFXToggleButton toggleMusicButton;
	@FXML
	private JFXSlider timeSlider;

	private MediaPlayer player;
	private MediaPlayer music;

	public void setSource(String source) {
		// Setup video player with source file
		File fileUrl = new File("videos/" + source + ".mp4"); 
		Media video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		screen.setMediaPlayer(player);
		slider();

		// Setup background music player
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

		// Custom indicator labelling
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
		music.setMute(!toggleMusicButton.isSelected());
	}

	@FXML
	private void quit() {
		timeLabel.getScene().getWindow().hide();
		shutdown();
	}

	public void shutdown() {
		player.dispose();
		music.dispose();
	}

}
