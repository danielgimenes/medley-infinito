package br.com.medleyinfinito.player.control;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.media.NoPlayerException;
import javax.media.Player;

import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public class MusicFilesPathPlayer extends MusicPlayer {

	private List<Player> jmfPlayers;
	
	private MusicFilesPathPlayer() {
		this.jmfPlayers = new ArrayList<Player>();
	}
	
	public MusicFilesPathPlayer(String... mp3FilePaths) throws NoPlayerException, MalformedURLException, IOException {
		this();
		for (String mp3FilePath : mp3FilePaths) {
			this.jmfPlayers.add(super.createJMFPlayer(mp3FilePath));
		}
	}
	
	@Override
	public void start() throws JMFPlayerNotInitializedException {
		for (Player jmfPlayer : jmfPlayers) {
			if (jmfPlayer == null) {
				throw new JMFPlayerNotInitializedException();
			}
			jmfPlayer.start();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			jmfPlayer.stop();
		}
	}

}
