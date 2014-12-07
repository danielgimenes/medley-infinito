package br.com.medleyinfinito.player;

import java.io.IOException;

import br.com.medleyinfinito.player.control.MusicDBSource;
import br.com.medleyinfinito.player.control.MusicFilesPathPlayer;
import br.com.medleyinfinito.player.control.MusicPlayer;
import br.com.medleyinfinito.player.control.MusicSourcePlayer;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		try {
			if (args.length > 0) {
				if (args[0].equals("-d")) {
					String musicFilesDirectory = args[1];
					MusicPlayer player = new MusicFilesPathPlayer(musicFilesDirectory);
					player.start();
					System.exit(0);
				} else {
					MusicPlayer player = new MusicFilesPathPlayer(new String[] { args[0] });
					player.start();
					System.exit(0);
				}
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
