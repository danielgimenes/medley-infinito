package br.com.medleyinfinito.player;

import br.com.medleyinfinito.player.control.MusicFilesSource;
import br.com.medleyinfinito.player.control.MusicFilesSourcePlayer;
import br.com.medleyinfinito.player.control.MusicPlayer;

public class Main {
	private static final String DEFAULT_MUSIC_FILES_DIRECTORY = "/home/daniel/Desenvolvimento/HackatonMusical2014/medley-infinito/baseMp3Desenv/short/";

	public static void main(String[] args) {
		String musicFilesDirectory = null;
		if (args.length > 0) {
			musicFilesDirectory = args[0];
		} else {
			musicFilesDirectory = DEFAULT_MUSIC_FILES_DIRECTORY;
		}
		try {
			MusicFilesSource source = new MusicFilesSource(musicFilesDirectory);
			MusicPlayer player = new MusicFilesSourcePlayer(source);

//			MusicPlayer player = new MusicFilesPathPlayer(DEFAULT_MUSIC_FILES_DIRECTORY + "03_-_Nemesis_short.mp3");

			player.start();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
