package br.com.medleyinfinito.player.control;

import java.io.File;

import br.com.medleyinfinito.player.exception.NoMusicFilesInDirException;
import br.com.medleyinfinito.player.exception.NotADirectoryException;

public class MusicFilesSource {

	private File[] musicFiles;
	private int musicFileIndex;

	public MusicFilesSource(String musicFilesDirectory) throws NotADirectoryException, NoMusicFilesInDirException {
		File musicDirectory = new File(musicFilesDirectory);
		if (!musicDirectory.isDirectory()) {
			throw new NotADirectoryException();
		}
		this.musicFiles = musicDirectory.listFiles();
		if (musicFiles.length == 0) {
			throw new NoMusicFilesInDirException();
		}
		this.musicFileIndex = 0;
	}

	public String getNextMusicFilePath() {
		if (this.musicFileIndex >= musicFiles.length) {
			return null;
		}
		return this.musicFiles[this.musicFileIndex++].getAbsolutePath();
	}

}
