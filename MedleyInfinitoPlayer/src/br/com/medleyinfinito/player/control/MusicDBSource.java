package br.com.medleyinfinito.player.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.medleyinfinito.player.exception.MusicPartNotFound;
import br.com.medleyinfinito.player.model.MusicPart;

public class MusicDBSource {

	private static final Integer TEMPO_TOLERANCE = 15;
	private MusicPart currentMusicPart;
	private MusicPart nextMusicPart;
	private Connection conn;

	public MusicDBSource() throws SQLException, MusicPartNotFound {
		this.currentMusicPart = fetchRandomMusicPart();
		this.nextMusicPart = null;
		startNextMusicFetcher();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private MusicPart fetchNextMusicPart() throws SQLException, MusicPartNotFound {
		String restriction = // .
				" filepath <> '" + this.currentMusicPart.getFilePath() + "'" + // .
				" AND keynote = " + this.currentMusicPart.getKeynote() + // .
				" AND (tempo BETWEEN " + Integer.toString(this.currentMusicPart.getTempo() - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString(this.currentMusicPart.getTempo() + TEMPO_TOLERANCE) + // .
				") OR (tempo BETWEEN " + Integer.toString((this.currentMusicPart.getTempo() / 2) - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString((this.currentMusicPart.getTempo() / 2) + TEMPO_TOLERANCE) + // .
				") OR (tempo BETWEEN " + Integer.toString((this.currentMusicPart.getTempo() * 2) - TEMPO_TOLERANCE) + // .
				" AND " + Integer.toString((this.currentMusicPart.getTempo() * 2) + TEMPO_TOLERANCE) + ")"; // .
		System.out.println(restriction);
		return this.fetchMusicPart(restriction);
	}

	private MusicPart fetchRandomMusicPart() throws SQLException, MusicPartNotFound {
		// TODO
		return this.fetchMusicPart();
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

	private MusicPart fetchMusicPart() throws SQLException, MusicPartNotFound {
		return this.fetchMusicPart(null);
	}

	private MusicPart fetchMusicPart(String restriction) throws SQLException, MusicPartNotFound {
		System.out.println("Connecting to DB");
		Connection connection = createDBConnection();
		String sql = "SELECT filepath, keynote, tempo FROM parts";
		if (restriction != null) {
			sql += " WHERE " + restriction;
		}
		System.out.println("Executing " + sql);
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setFetchSize(1);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			String filePath = results.getString("filepath");
			int keynote = results.getInt("keynote");
			int tempo = results.getInt("tempo");
			results.close();
			MusicPart musicPart = new MusicPart(filePath, keynote, tempo);
			System.out.println("fecth " + musicPart.toString());
			return musicPart;
		} else {
			results.close();
			System.out.println("MusicPartNotFound");
			throw new MusicPartNotFound();
		}

	}

	private void startNextMusicFetcher() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("NextMusicFetcher started");
				while (true) {
					if (MusicDBSource.this.nextMusicPart == null) {
						try {
							System.out.println("Fecthing next music");
							MusicDBSource.this.nextMusicPart = fetchNextMusicPart();
							System.out.println("next music fetch");
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

	public File getNextMusicFile() {
		if (this.nextMusicPart == null) {
			return null;
		}
		try {
			File file = this.nextMusicPart.getFile();
			this.currentMusicPart = this.nextMusicPart;
			this.nextMusicPart = null;
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
