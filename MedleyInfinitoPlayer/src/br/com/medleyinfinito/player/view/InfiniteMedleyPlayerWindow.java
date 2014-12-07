package br.com.medleyinfinito.player.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import br.com.medleyinfinito.player.control.MusicDBSource;
import br.com.medleyinfinito.player.control.MusicPlayer;
import br.com.medleyinfinito.player.control.MusicSourcePlayer;
import br.com.medleyinfinito.player.exception.MusicPartNotFound;
import br.com.medleyinfinito.player.model.MusicPart;

public class InfiniteMedleyPlayerWindow {
	private static final Color HIGHLIGHT_COLOR = new Color(250, 165, 25);
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
	private static final Color FOREGROUND_COLOR = new Color(46, 94, 135);
	private JFrame frame;
	private JLabel titleLabel;
	private JPanel mainPanel;
	private JPanel bluePanel;
	private JPanel whitePanel;
	private JLabel artistLabel;
	private JLabel keyLabel;
	private JLabel tempoLabel;
	private JLabel nextLabel;
	private JLabel nextTitleLabel;
	private JLabel nextArtistLabel;
	private MusicDBSource source;
	private JLabel albumImage;
	private JLabel nextAlbumImage;

	public InfiniteMedleyPlayerWindow() {
	}

	public void run() throws SQLException, MusicPartNotFound, MalformedURLException, IOException {
		setupGUI();
		startMusicPlayer();
	}

	private void startMusicPlayer() throws SQLException, MusicPartNotFound, MalformedURLException, IOException {
		source = new MusicDBSource();
		source.addNextMusicListener(new MusicDBSource.NextMusicListener() {
			@Override
			public void musicChanged() {
				try {
					InfiniteMedleyPlayerWindow.this.updateGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		MusicPlayer player = new MusicSourcePlayer(source);
		player.start();
		try {
			Thread.sleep(MusicPlayer.FADE_OUT_DURATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startMusicPlayer();
	}

	private void setupGUI() throws IOException {
		frame = new JFrame("Infinite Medley");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setIconImage(new ImageIcon("/mp3/logo_infinite_medley.png").getImage());
		centerWindow();
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(BACKGROUND_COLOR);
		bluePanel = new JPanel();
		bluePanel.setLayout(null);
		bluePanel.setBackground(FOREGROUND_COLOR);
		whitePanel = new JPanel();
		whitePanel.setLayout(null);
		whitePanel.setBackground(BACKGROUND_COLOR);

		albumImage = new JLabel("");

		nextAlbumImage = new JLabel("");

		BufferedImage logoPic = ImageIO.read(new File("/mp3/logo_infinite_medley_transparent.png"));
		JLabel logo = new JLabel(new ImageIcon(getScaledImage(logoPic, 200, 130)));

		titleLabel = new JLabel("<Título>");
		titleLabel.setFont(new Font("Sans", Font.PLAIN, 48));
		titleLabel.setForeground(HIGHLIGHT_COLOR);

		artistLabel = new JLabel("<Artista>");
		artistLabel.setFont(new Font("Sans", Font.PLAIN, 38));
		artistLabel.setForeground(HIGHLIGHT_COLOR);

		nextTitleLabel = new JLabel("<Título>");
		nextTitleLabel.setFont(new Font("Sans", Font.PLAIN, 28));
		nextTitleLabel.setForeground(FOREGROUND_COLOR);

		nextArtistLabel = new JLabel("<Artista>");
		nextArtistLabel.setFont(new Font("Sans", Font.PLAIN, 28));
		nextArtistLabel.setForeground(FOREGROUND_COLOR);

		keyLabel = new JLabel("<Key>");
		keyLabel.setFont(new Font("Sans", Font.PLAIN, 32));
		keyLabel.setForeground(HIGHLIGHT_COLOR);

		tempoLabel = new JLabel("<Tempo>");
		tempoLabel.setFont(new Font("Sans", Font.PLAIN, 32));
		tempoLabel.setForeground(HIGHLIGHT_COLOR);

		nextLabel = new JLabel("a seguir:");
		nextLabel.setFont(new Font("Sans", Font.PLAIN, 30));
		nextLabel.setForeground(FOREGROUND_COLOR);

		Insets insets = mainPanel.getInsets();
		mainPanel.setSize(1200, 750);

		frame.add(bluePanel);
		bluePanel.setBounds(0, 0, 1300, 350);

		frame.add(whitePanel);
		whitePanel.setBounds(0, 800, 1200, 350);

		frame.add(albumImage);
		Border border = BorderFactory.createLineBorder(BACKGROUND_COLOR, 10);
		albumImage.setBorder(border);
		albumImage.setBounds(40 + insets.left, 40 + insets.top, 670, 670);

		frame.add(nextAlbumImage);
		Border nextBorder = BorderFactory.createLineBorder(BACKGROUND_COLOR, 5);
		nextAlbumImage.setBorder(nextBorder);
		nextAlbumImage.setBounds(730, 450, 230, 230);

		frame.add(titleLabel);
		titleLabel.setBounds(750, 60, 480, 50);

		frame.add(artistLabel);
		artistLabel.setBounds(750, 140, 480, 50);

		frame.add(keyLabel);
		keyLabel.setBounds(750, 220, 480, 50);

		frame.add(tempoLabel);
		tempoLabel.setBounds(750, 280, 480, 50);

		frame.add(nextLabel);
		nextLabel.setBounds(750, 380, 480, 50);

		frame.add(nextTitleLabel);
		nextTitleLabel.setBounds(980, 480, 480, 50);

		frame.add(nextArtistLabel);
		nextArtistLabel.setBounds(980, 550, 480, 50);

		frame.add(logo);
		logo.setBounds(0, 10, 270, 170);

		int i = 1;
		frame.setComponentZOrder(logo, i++);
		frame.setComponentZOrder(titleLabel, i++);
		frame.setComponentZOrder(artistLabel, i++);
		frame.setComponentZOrder(keyLabel, i++);
		frame.setComponentZOrder(tempoLabel, i++);
		frame.setComponentZOrder(albumImage, i++);
		frame.setComponentZOrder(nextAlbumImage, i++);
		frame.setComponentZOrder(nextLabel, i++);
		frame.setComponentZOrder(nextTitleLabel, i++);
		frame.setComponentZOrder(nextArtistLabel, i++);
		frame.setComponentZOrder(bluePanel, i++);
		frame.setComponentZOrder(whitePanel, i++);
		frame.getContentPane().add(mainPanel);
		frame.setSize(1300, 750);
		frame.setVisible(true);
	}

	protected void updateGUI() throws IOException {
		MusicPart currentMusic = source.getDefinedCurrentMusic();
		MusicPart nextMusic = source.getDefinedNextMusic();
		titleLabel.setText(currentMusic.getName());
		artistLabel.setText(currentMusic.getArtist());
		keyLabel.setText(currentMusic.getKey_right());
		tempoLabel.setText(Integer.toString(currentMusic.getTempo()) + " bpm");
		if (currentMusic != null && currentMusic.getCover() != null && currentMusic.getCover().trim().length() > 0) {
			BufferedImage albumPicture = ImageIO.read(new URL(currentMusic.getCover()));
			albumImage.setIcon(new ImageIcon(getScaledImage(albumPicture, 700, 700)));
		} else {
			albumImage.setIcon(null);
			albumImage.setForeground(BACKGROUND_COLOR);
			albumImage.setBackground(BACKGROUND_COLOR);
		}

		if (nextMusic == null) {
			nextTitleLabel.setText("");
			nextArtistLabel.setText("");
			nextAlbumImage.setIcon(null);
			nextAlbumImage.setForeground(BACKGROUND_COLOR);
			return;
		}
		nextTitleLabel.setText(nextMusic.getName());
		nextArtistLabel.setText(nextMusic.getArtist());
		if (nextMusic.getCover() != null && nextMusic.getCover().trim().length() > 0) {
			BufferedImage nextAlbumPicture = ImageIO.read(new URL(nextMusic.getCover()));
			nextAlbumImage.setIcon(new ImageIcon(getScaledImage(nextAlbumPicture, 230, 230)));
		} else {
			nextAlbumImage.setIcon(null);
			nextAlbumImage.setForeground(BACKGROUND_COLOR);
			nextAlbumImage.setBackground(BACKGROUND_COLOR);
		}

	}

	private void centerWindow() {
		frame.setLocationRelativeTo(null);
		frame.setLocation(380, 160);
	}

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

}
