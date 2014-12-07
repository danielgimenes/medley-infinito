package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.medleyinfinito.player.exception.NoMusicFilesInDirException;
import br.com.medleyinfinito.player.exception.NotADirectoryException;
import br.com.medleyinfinito.player.model.MusicPart;

public class MusicFilesPathPlayer extends MusicPlayer {

	private List<String> musicFiles;

	private MusicFilesPathPlayer() {
		this.musicFiles = new ArrayList<String>();
	}

	public MusicFilesPathPlayer(String musicFilesDirectory) throws MalformedURLException, IOException,
			NoMusicFilesInDirException, NotADirectoryException {
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
			this.musicFiles.add(musicFile.getAbsolutePath());
		}
	}

	public MusicFilesPathPlayer(String[] musicFilePaths) throws MalformedURLException, IOException {
		this();
		for (String mp3FilePath : musicFilePaths) {
			System.out.println("Processing file " + mp3FilePath);
			this.musicFiles.add(mp3FilePath);
		}
	}

	@Override
	public void start() throws IOException {
		for (String musicFile : this.musicFiles) {
			super.playUntilEnd(new MusicPart(musicFile, "", 0, 0, 20, "", "", "", ""));
		}
	}

}
