package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.FloatControl;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public abstract class MusicPlayer {

	private boolean musicIsPlaying;

	public abstract void start() throws JMFPlayerNotInitializedException, MalformedURLException, IOException,
			JavaLayerException;

	protected AdvancedPlayer createAdvancedPlayer(String mp3FilePath) throws MalformedURLException, IOException,
			JavaLayerException {
		File mp3File = new File(mp3FilePath);
		if (!mp3File.exists()) {
			throw new FileNotFoundException();
		}
		FileInputStream in = new FileInputStream(mp3FilePath);
		AdvancedPlayer player = new AdvancedPlayer(in);
		return player;
	}

	protected void playUntilEnd(AdvancedPlayer player) throws JavaLayerException {
		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackFinished(PlaybackEvent event) {
				MusicPlayer.this.musicIsPlaying = false;
			}
		});
		System.out.println("starting...");
		this.musicIsPlaying = true;
		player.play();
		while (this.musicIsPlaying) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("stopping...");
		player.close();
	}

}