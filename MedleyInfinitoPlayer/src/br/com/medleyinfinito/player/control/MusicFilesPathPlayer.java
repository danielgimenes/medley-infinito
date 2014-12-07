package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.media.NoPlayerException;
import javax.media.Player;

import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;
import br.com.medleyinfinito.player.exception.NoMusicFilesInDirException;
import br.com.medleyinfinito.player.exception.NotADirectoryException;

public class MusicFilesPathPlayer extends MusicPlayer {

	private List<Player> jmfPlayers;

	private MusicFilesPathPlayer() {
		this.jmfPlayers = new ArrayList<Player>();
	}

	public MusicFilesPathPlayer(String musicFilesDirectory) throws NoPlayerException, MalformedURLException,
			IOException, NoMusicFilesInDirException, NotADirectoryException {
		this();
		File musicDirectory = new File(musicFilesDirectory);
		if (!musicDirectory.isDirectory()) {
			throw new NotADirectoryException();
		}
		File[] musicFiles = musicDirectory.listFiles();
		if (musicFiles.length == 0) {
			throw new NoMusicFilesInDirException();
		}
		for (File musicFile : musicFiles) {
			this.jmfPlayers.add(super.createJMFPlayer(musicFile.getAbsolutePath()));
		}
	}

	public MusicFilesPathPlayer(String[] musicFilePaths) throws NoPlayerException, MalformedURLException, IOException {
		this();
		for (String mp3FilePath : musicFilePaths) {
			this.jmfPlayers.add(super.createJMFPlayer(mp3FilePath));
		}
	}

	@Override
	public void start() throws JMFPlayerNotInitializedException {
		for (Player jmfPlayer : jmfPlayers) {
			if (jmfPlayer == null) {
				throw new JMFPlayerNotInitializedException();
			}
			super.playUntilEnd(jmfPlayer);
		}
	}

}
