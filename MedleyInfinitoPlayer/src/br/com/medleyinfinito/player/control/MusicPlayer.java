package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;

import br.com.medleyinfinito.player.exception.JMFPlayerNotInitializedException;

public abstract class MusicPlayer {

	public abstract void start() throws JMFPlayerNotInitializedException, NoPlayerException, MalformedURLException,
			IOException;

	protected Player createJMFPlayer(String mp3FilePath) throws NoPlayerException, MalformedURLException, IOException {
		File mp3File = new File(mp3FilePath);
		if (!mp3File.exists()) {
			throw new FileNotFoundException();
		}
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder", new Format[] { input1, input2 },
				new Format[] { output }, PlugInManager.CODEC);
		return Manager.createPlayer(new MediaLocator(mp3File.toURI().toURL()));
	}

}