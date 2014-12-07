package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;
import br.com.medleyinfinito.player.exception.NoMusicFilesInDirException;
import br.com.medleyinfinito.player.exception.NotADirectoryException;

public class MusicFilesPathPlayer extends MusicPlayer {

	private List<AdvancedPlayer> players;

	private MusicFilesPathPlayer() {
		this.players = new ArrayList<AdvancedPlayer>();
	}

	public MusicFilesPathPlayer(String musicFilesDirectory) throws MalformedURLException,
			IOException, NoMusicFilesInDirException, NotADirectoryException, JavaLayerException {
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
			System.out.println("Processing file " + musicFile.getPath());
			this.players.add(super.createAdvancedPlayer(musicFile.getAbsolutePath()));
		}
	}

	public MusicFilesPathPlayer(String[] musicFilePaths) throws MalformedURLException, IOException, JavaLayerException {
		this();
		for (String mp3FilePath : musicFilePaths) {
			System.out.println("Processing file " + mp3FilePath);
			this.players.add(super.createAdvancedPlayer(mp3FilePath));
		}
	}

	@Override
	public void start() throws JMFPlayerNotInitializedException, JavaLayerException {
		for (AdvancedPlayer player : players) {
			if (player == null) {
				throw new JMFPlayerNotInitializedException();
			}
			super.playUntilEnd(player);
		}
	}

}
