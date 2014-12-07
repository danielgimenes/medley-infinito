package br.com.medleyinfinito.player.control;

import java.io.File;
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

	private static final Integer TEMPO_TOLERANCE = 5;
	private List<MusicPart> alreadyPlayed;
	private MusicPart currentMusicPart;
	private MusicPart nextMusicPart;
	private Connection conn;

	public MusicDBSource() throws SQLException, MusicPartNotFound {
		alreadyPlayed = new ArrayList<MusicPart>();
		this.currentMusicPart = fetchRandomMusicPart();
		this.nextMusicPart = null;
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
		System.out.println("fetchNextMusicPart() restriction = " + restriction);
		return this.fetchMusicPart(restriction);
	}

	private MusicPart fetchRandomMusicPart() throws SQLException, MusicPartNotFound {
		MusicPart musicPart = null;
		while (musicPart == null) {
			int keynote = new Random().nextInt() % 24;
			if (keynote < 0) {
				keynote *= -1;
			}
			System.out.println("fetchRandomMusicPart() keynote = " + keynote);
			musicPart = this.fetchMusicPart(" keynote = " + keynote);
		}
		return musicPart;
	}

	private Connection createDBConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed()) {
			System.out.println("createDBConnection()");
			String url = "jdbc:postgresql://localhost:5432/medleyinfinito_db";
			String user = "postgres";
			String password = "cogitoR341";
			this.conn = DriverManager.getConnection(url, user, password);
			System.out.println("createDBConnection() ok");
		}
		return this.conn;
	}

	private MusicPart fetchMusicPart(String restriction) throws SQLException, MusicPartNotFound {
		System.out.println("Connecting to DB");
		Connection connection = createDBConnection();
		String sql = "SELECT filepath, originalfile, keynote, tempo FROM parts";
		if (restriction != null) {
			sql += " WHERE " + restriction;
		}
		System.out.println("fetchMusicPart(String restriction) sql = " + sql);
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet results = statement.executeQuery();
		List<MusicPart> musicParts = new ArrayList<MusicPart>();
		while (results.next()) {
			String filePath = results.getString("filepath");
			String originalFile = results.getString("originalfile");
			int keynote = results.getInt("keynote");
			int tempo = results.getInt("tempo");
			musicParts.add(new MusicPart(filePath, originalFile, keynote, tempo));
		}
		if (musicParts.size() == 0) {
			return null;
		}
		results.close();
		System.out.println("fetchMusicPart(String restriction) fetch " + musicParts.size() + " music parts");
		List<MusicPart> musicPartsToIgnore = new ArrayList<MusicPart>();
		for (MusicPart musicPart : musicParts) {
			if (alreadyPlayed.contains(musicPart)) {
				musicPartsToIgnore.add(musicPart);
			}
		}
		for (MusicPart musicPart : musicPartsToIgnore) {
			musicParts.remove(musicPart);
		}
		if (musicParts.size() == 0) {
			return null;
		}
		int index = new Random().nextInt(musicParts.size());
		index = index < 0 ? 0 : index;
		System.out.println("fetchMusicPart(String restriction) index = " + index);
		MusicPart musicPart = musicParts.get(index);
		System.out.println("fetchMusicPart(String restriction) choose " + musicPart.getFilePath());
		return musicPart;

	}

	private void startNextMusicFetcher() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("NextMusicFetcher started");
				while (true) {
					if (MusicDBSource.this.nextMusicPart == null) {
						try {
							System.out.println("Fetching next music");
							MusicDBSource.this.nextMusicPart = fetchNextMusicPart();
							if (MusicDBSource.this.nextMusicPart == null) {
								System.out.println("No match music part found!");
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

	public File getNextMusicFile() throws FileNotFoundException {
		if (this.alreadyPlayed.size() == 0) {
			this.alreadyPlayed.add(this.currentMusicPart);
			return this.currentMusicPart.getFile();
		}
		if (this.nextMusicPart == null) {
			return null;
		}
		try {
			File file = this.nextMusicPart.getFile();
			this.alreadyPlayed.add(this.currentMusicPart);
			this.currentMusicPart = this.nextMusicPart;
			this.nextMusicPart = null;
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
