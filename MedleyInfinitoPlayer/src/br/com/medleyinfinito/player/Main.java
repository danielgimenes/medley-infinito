package br.com.medleyinfinito.player;

import br.com.medleyinfinito.player.control.MusicDBSource;
import br.com.medleyinfinito.player.control.MusicFilesPathPlayer;
import br.com.medleyinfinito.player.control.MusicSourcePlayer;
import br.com.medleyinfinito.player.control.MusicPlayer;

public class Main {
	
	public static void main(String[] args) {
		String musicFilesDirectory = null;
		try {
			if (args.length > 0) {
				musicFilesDirectory = args[0];
				MusicPlayer player = new MusicFilesPathPlayer(musicFilesDirectory);
				player.start();
				System.exit(0);
			} else {
				MusicDBSource source = new MusicDBSource();
				MusicPlayer player = new MusicSourcePlayer(source);
				player.start();
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
