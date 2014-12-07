package br.com.medleyinfinito.player.control;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.medleyinfinito.player.model.MusicPart;

public abstract class MusicPlayer {

	private static final Integer FADE_OUT_DURATION = 10;
	private static final Integer DEFAULT_SLEEP_DURATION = 20;

	public abstract void start() throws MalformedURLException, IOException;

	protected void playUntilEnd(MusicPart musicPart) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("cvlc", "--no-loop", musicPart.getFilePath());
		System.out.println("starting...");
		Process p = pb.start();
		int sleepDurationInSeconds;
		if (musicPart.getDuration() > 0) {
			Integer durationWithoutFadeOut = musicPart.getDuration() - FADE_OUT_DURATION;
			sleepDurationInSeconds = (durationWithoutFadeOut > 0 ? durationWithoutFadeOut : 0);
		} else {
			sleepDurationInSeconds = DEFAULT_SLEEP_DURATION;
		}
		try {
			System.out.println("Waiting for " + sleepDurationInSeconds + " seconds");
			Thread.sleep(sleepDurationInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("fading out...");
		new Thread(new ProcessCleaner(p)).start();
	}
	
	class ProcessCleaner implements Runnable {
		private Process p;

		public ProcessCleaner(Process p) {
			this.p = p;
		}

		@Override
		public void run() {
			while (p.isAlive()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			p.destroy();
		}
		
	};

}