package br.com.medleyinfinito.player.control;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.medleyinfinito.player.exception.MusicPartNotFound;
import br.com.medleyinfinito.player.model.MusicPart;

public class MusicDBSource {

	public interface MusicPlayerEventListener {
		public void currentMusicChanged(MusicPart currentMusic);

		public void nextMusicDefined(MusicPart nextMusic);
	}

	private static final Integer TEMPO_TOLERANCE = 15;
	private List<MusicPart> alreadyPlayed;
	private MusicPart currentMusicPart;
	private MusicPart nextMusicPart;
	private Connection conn;
	private List<MusicPlayerEventListener> listeners;
	private boolean fetchNextMusic = false;

	public MusicDBSource() throws SQLException, MusicPartNotFound {
		System.out.println("Starting player...");
		listeners = new ArrayList<MusicPlayerEventListener>();
		alreadyPlayed = new ArrayList<MusicPart>();
		System.out.println("Fetching first music");
		this.currentMusicPart = fetchRandomMusicPart();
		startNextMusicFetcher();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private MusicPart fetchNextMusicPart() throws SQLException, MusicPartNotFound {
		String restriction = // .
		" filepath <> '" + this.currentMusicPart.getFilePath() + "'" + // .
				" AND originalfile <> '" + this.currentMusicPart.getOriginalFile() + "'" + // .
				" AND keynote = " + this.currentMusicPart.getKeynote() + // .
				" AND (tempo BETWEEN " + Integer.toString(this.currentMusicPart.getTempo() - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString(this.currentMusicPart.getTempo() + TEMPO_TOLERANCE) + // .
				") OR (tempo BETWEEN " + Integer.toString((this.currentMusicPart.getTempo() / 2) - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString((this.currentMusicPart.getTempo() / 2) + TEMPO_TOLERANCE) + // .
				") OR (tempo BETWEEN " + Integer.toString((this.currentMusicPart.getTempo() * 2) - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString((this.currentMusicPart.getTempo() * 2) + TEMPO_TOLERANCE) + ")"; // .
		MusicPart next = this.fetchMusicPart(restriction);
		System.out.println("Next Music = '" + next.getName() + "' by '" + next.getArtist() + "'");
		emitNextMusicDefinedEvent(next);
		return next;
	}

	private MusicPart fetchRandomMusicPart() throws SQLException, MusicPartNotFound {
		MusicPart musicPart = null;
		while (musicPart == null) {
			int keynote = new Random().nextInt() % 24;
			if (keynote < 0) {
				keynote *= -1;
			}
			musicPart = this.fetchMusicPart(" keynote = " + keynote);
		}
		System.out.println("First Music = '" + musicPart.getName() + "' by '" + musicPart.getArtist() + "'");
		return musicPart;
	}

	private Connection createDBConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed()) {
			String url = "jdbc:postgresql://localhost:5432/medleyinfinito_db";
			String user = "postgres";
			String password = "cogitoR341";
			this.conn = DriverManager.getConnection(url, user, password);
		}
		return this.conn;
	}

	private MusicPart fetchMusicPart(String restriction) throws SQLException, MusicPartNotFound {
		Connection connection = createDBConnection();
		String sql = "SELECT filepath, originalfile, keynote, tempo, duration, artist, cover, name, right_key FROM parts";
		if (restriction != null) {
			sql += " WHERE " + restriction;
		}
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet results = statement.executeQuery();
		List<MusicPart> musicParts = new ArrayList<MusicPart>();
		while (results.next()) {
			String filePath = results.getString("filepath");
			String originalFile = results.getString("originalfile");
			int keynote = results.getInt("keynote");
			int tempo = results.getInt("tempo");
			int duration = results.getInt("duration");
			String name = results.getString("name");
			String artist = results.getString("artist");
			String cover = results.getString("cover");
			String right_key = results.getString("right_key");
			musicParts.add(new MusicPart(filePath, originalFile, keynote, tempo, duration, name, artist, right_key,
					cover));
		}
		results.close();
		List<MusicPart> musicPartsToIgnore = new ArrayList<MusicPart>();
		for (MusicPart musicPart : musicParts) {
			if (alreadyPlayed.contains(musicPart)) {
				musicPartsToIgnore.add(musicPart);
			}
		}
		for (MusicPart musicPart : musicPartsToIgnore) {
			musicParts.remove(musicPart);
		}
		System.out.println("Possible continuations: " + musicParts.size());
		if (musicParts.size() == 0) {
			return null;
		}
		int index = new Random().nextInt(musicParts.size());
		index = index < 0 ? 0 : index;
		MusicPart musicPart = musicParts.get(index);
		return musicPart;

	}

	private void startNextMusicFetcher() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (MusicDBSource.this.fetchNextMusic == true) {
						try {
							System.out.println("Fetching next music");
							MusicDBSource.this.fetchNextMusic = false;
							MusicDBSource.this.nextMusicPart = fetchNextMusicPart();
							if (MusicDBSource.this.nextMusicPart == null) {
								System.out.println("No matching music part found!");
								System.out.println("Stopping player...");
								return;
							}
						} catch (SQLException | MusicPartNotFound e) {
							e.printStackTrace();
						}
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

	public MusicPart getNextMusic() throws FileNotFoundException {
		if (this.alreadyPlayed.size() == 0) {
			this.alreadyPlayed.add(this.currentMusicPart);
			emitCurrentMusicChangedEvent(this.currentMusicPart);
			this.fetchNextMusic = true;
			return this.currentMusicPart;
		}
		if (this.nextMusicPart == null) {
			return null;
		}
		MusicPart musicPart = this.nextMusicPart;
		this.alreadyPlayed.add(this.currentMusicPart);
		this.currentMusicPart = this.nextMusicPart;
		this.nextMusicPart = null;
		emitCurrentMusicChangedEvent(this.currentMusicPart);
		this.fetchNextMusic = true;
		return musicPart;
	}

	private void emitCurrentMusicChangedEvent(MusicPart currentMusic) {
		for (MusicPlayerEventListener listener : this.listeners) {
			listener.currentMusicChanged(currentMusic);
		}
	}

	private void emitNextMusicDefinedEvent(MusicPart nextMusic) {
		for (MusicPlayerEventListener listener : this.listeners) {
			listener.nextMusicDefined(nextMusic);
		}
	}

	public void addMusicPlayerEventListener(MusicPlayerEventListener nextMusicListener) {
		listeners.add(nextMusicListener);

	}

	public MusicPart getDefinedCurrentMusic() {
		return this.currentMusicPart;
	}

	public MusicPart getDefinedNextMusic() {
		return this.nextMusicPart;
	}
}
