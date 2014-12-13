package br.com.medleyinfinito.player.control;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.medleyinfinito.player.model.MusicPart;

public class MusicSourcePlayer extends MusicPlayer {

	private MusicDBSource source;

	public MusicSourcePlayer(MusicDBSource source) throws MalformedURLException, IOException {
		this.source = source;
	}

	@Override
	public void start() throws MalformedURLException, IOException {
		MusicPart musicFile = null;
		while ((musicFile = source.getNextMusic()) != null) {
			super.playUntilEnd(musicFile);
		}
	}
}
