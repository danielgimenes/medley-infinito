package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.NoPlayerException;
import javax.media.Player;

import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public class MusicFilesSourcePlayer extends MusicPlayer {

	private MusicFilesSource source;
	private Player jmfPlayer;
	
	public MusicFilesSourcePlayer(MusicFilesSource source) throws NoPlayerException, MalformedURLException, IOException {
		this.source = source;
	}

	@Override
	public void start() throws JMFPlayerNotInitializedException, NoPlayerException, MalformedURLException, IOException {
		File musicFile = null;
		while ((musicFile = source.getNextMusicFile()) != null) {
			System.out.println("Playing file " + musicFile.getAbsolutePath());
			this.jmfPlayer = super.createJMFPlayer(musicFile.getAbsolutePath());
			this.jmfPlayer.start();
			while (this.jmfPlayer.getState() != Player.Realized) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.jmfPlayer.stop();
			this.jmfPlayer.close();
			this.jmfPlayer.deallocate();
		}
	}
}
