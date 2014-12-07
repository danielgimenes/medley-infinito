package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.FileNotFoundException;

import br.com.medleyinfinito.player.model.MusicPart;

public class MusicDBSource {

	private MusicPart currentMusicPart;
	private MusicPart nextMusicPart;

	public MusicDBSource() {
		this.currentMusicPart = fetchRandomMusicPart();
		startNextMusicFetcher();
	}

	private MusicPart fetchNextMusicPart() {
		// TODO Auto-generated method stub
		
	}

	private MusicPart fetchRandomMusicPart() {
		// TODO Auto-generated method stub
		
	}

	private void startNextMusicFetcher() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (MusicDBSource.this.nextMusicPart == null) {
						MusicDBSource.this.nextMusicPart = fetchNextMusicPart();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public File getNextMusicFile() {
		if (nextMusicPart == null) {
			return null;
		}
		try {
			return nextMusicPart.getFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
