package app.controllers.helpers;

import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import app.DialogBuilder;
import app.VideoCreation;
import app.WindowBuilder;
import app.controllers.RatingController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * Handles functionality for the HomeController class
 */
public class ReviewControllerHelper {
	
	/**
	 * Update playlist and upcoming video
	 */
	public void updatePlaylist(Label upcomingLabel, ArrayList<VideoCreation> playList, ListView playListView, int playIndex) {
		// Update upcoming label
		if ((playIndex+1) == playList.size()) upcomingLabel.setText("Upcoming: None." );
		else upcomingLabel.setText("Upcoming: " + playList.get(playIndex+1).getName());
		// Select the currently playing video
		playListView.getSelectionModel().select(playIndex);
	}

	/**
	 * Update the transcript of the video currently playing
	 */
	public void updateTranscript(VideoCreation currentVideo, JFXTextArea transcript) {
		String transcriptString = "";
		for(String line: currentVideo.getTextContent()) {
			transcriptString += line.trim() + "\n";
		}
		transcript.setText(transcriptString);
	}

	/**
	 * Checks the current status of the favourite aspect of the currently playing video, and set up the star icon as such
	 */
	public void setUpStar(VideoCreation currentVideo, MaterialDesignIconView favHeart) {
		if (currentVideo.getFavourite()) {
			favHeart.setIcon(MaterialDesignIcon.HEART);
		} else {
			favHeart.setIcon(MaterialDesignIcon.HEART_OUTLINE);
		}
	}
	
	
	/**
	 * Reset the buttons for play and pause to their default values
	 */
	public void resetControlButtons(JFXButton playButton, MaterialDesignIconView playIcon, JFXButton muteButton, MaterialDesignIconView muteIcon) {
		// Reset okay and mute buttons
		playButton.setText("Pause");
		playIcon.setStyle("-glyph-name:PAUSE");
		muteButton.setText("Mute");
		muteIcon.setStyle("-glyph-name:VOLUME_OFF");
	}

	/**
	 * Set up the time slider for videos
	 */
	public void slider(MediaPlayer player, JFXSlider timeSlider, JFXButton playButton, MaterialDesignIconView playIcon) {
		// Providing functionality to time slider
		player.currentTimeProperty().addListener(ov -> updatesValues(player, timeSlider));

		// In order to jump to the certain part of video
		timeSlider.valueProperty().addListener(ov-> {
			if (timeSlider.isPressed()) {
				player.pause();
				double percent = timeSlider.getValue()/player.getTotalDuration().toSeconds();
				player.seek(player.getTotalDuration().multiply(percent));
			}
		});
		timeSlider.setOnMouseReleased(e->{
			player.play();
			switchPlayIcon(player, playButton, playIcon);
		});
		timeSlider.setOnDragDropped(e-> {
			player.play();
			switchPlayIcon(player, playButton, playIcon);
		});

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

	/**
	 * Set the time on the time slider
	 */
	private void updatesValues(MediaPlayer player, JFXSlider timeSlider) {
		Platform.runLater(()-> {
			timeSlider.setMax(player.getTotalDuration().toSeconds());
			timeSlider.setValue(player.getCurrentTime().toSeconds());
		});
	}
	
	/**
	 * Toggle mute on the video player, and switch icons when this happens
	 */
	public void mute(MediaPlayer player, JFXButton muteButton, MaterialDesignIconView muteIcon) {
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
	
	/**
	 *  Switch the play/pause icons depending on whether the video is playing or not
	 */
	public void switchPlayIcon(MediaPlayer player, JFXButton playButton, MaterialDesignIconView playIcon) {
		if (player.getStatus() == Status.PLAYING) {
			playButton.setText("Play");
			playIcon.setStyle("-glyph-name:PLAY");
		} else {
			playButton.setText("Pause");
			playIcon.setStyle("-glyph-name:PAUSE");
		}
	}
	
	/**
	 * Allows user to rate the videos as they watch them
	 */
	public void showRating(ArrayList<VideoCreation> playList, int playIndex, StackPane stackPane) {
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
	
	/**
	 * Make the label for time update as the video plays
	 */
	public void updateTimeLabel(MediaPlayer player, Label timeLabel) {
		player.currentTimeProperty().addListener((observable,oldValue,newValue) -> {
			String time = "";
			time += String.format("%02d", (int)newValue.toMinutes());
			time += ":";
			time += String.format("%02d", (int)newValue.toSeconds()%60);
			timeLabel.setText(time);
		});
	}
}
