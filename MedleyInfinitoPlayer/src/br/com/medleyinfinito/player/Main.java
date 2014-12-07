package br.com.medleyinfinito.player;

import java.io.IOException;

import br.com.medleyinfinito.player.control.MusicFilesPathPlayer;
import br.com.medleyinfinito.player.control.MusicPlayer;
import br.com.medleyinfinito.player.view.InfiniteMedleyPlayerWindow;

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
				new InfiniteMedleyPlayerWindow().run();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
