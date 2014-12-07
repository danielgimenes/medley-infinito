package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public class MusicSourcePlayer extends MusicPlayer {

	private MusicDBSource source;

	public MusicSourcePlayer(MusicDBSource source) throws MalformedURLException, IOException {
		this.source = source;
	}

	@Override
	public void start() throws JMFPlayerNotInitializedException, MalformedURLException, IOException, JavaLayerException {
		File musicFile = null;
		while ((musicFile = source.getNextMusicFile()) != null) {
			System.out.println("Playing file " + musicFile.getPath());
			AdvancedPlayer player = super.createAdvancedPlayer(musicFile.getAbsolutePath());
			super.playUntilEnd(player);
		}
	}
}
