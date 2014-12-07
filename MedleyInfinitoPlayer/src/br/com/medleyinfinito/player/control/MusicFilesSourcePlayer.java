package br.com.medleyinfinito.player.control;

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
		String musicFilePath = null;
		while ((musicFilePath = source.getNextMusicFilePath()) != null) {
			System.out.println("Playing file " + musicFilePath);
			this.jmfPlayer = super.createJMFPlayer(musicFilePath);
			this.jmfPlayer.start();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.jmfPlayer.stop();
			this.jmfPlayer.close();
			this.jmfPlayer.deallocate();
		}
	}

}
