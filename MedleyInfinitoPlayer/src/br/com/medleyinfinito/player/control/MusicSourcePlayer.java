package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.NoPlayerException;
import javax.media.Player;

import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public class MusicSourcePlayer extends MusicPlayer {

	private MusicDBSource source;
	private Player jmfPlayer;

	public MusicSourcePlayer(MusicDBSource source) throws NoPlayerException, MalformedURLException, IOException {
		this.source = source;
	}

	@Override
	public void start() throws JMFPlayerNotInitializedException, NoPlayerException, MalformedURLException, IOException {
		File musicFile = null;
		while ((musicFile = source.getNextMusicFile()) != null) {
			System.out.println("Playing file " + musicFile.getPath());
			this.jmfPlayer = super.createJMFPlayer(musicFile.getAbsolutePath());
			super.playUntilEnd(this.jmfPlayer);
		}
	}
}