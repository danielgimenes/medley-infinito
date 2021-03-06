package br.com.medleyinfinito.player.model;

import java.io.File;
import java.io.FileNotFoundException;

public class MusicPart {
	private String filePath;
	private String originalFile;
	private Integer keynote;
	private Integer tempo;
	private Integer duration;
	private String name;
	private String artist;
	private String key_right;
	private String cover;

	public MusicPart(String filePath, String originalFile, Integer keynote, Integer tempo, Integer duration,
			String name, String artist, String key_right, String cover) {
		super();
		this.filePath = filePath;
		this.originalFile = originalFile;
		this.keynote = keynote;
		this.tempo = tempo;
		this.duration = duration;
		this.name = name;
		this.artist = artist;
		this.key_right = key_right;
		this.setCover(cover);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getKeynote() {
		return keynote;
	}

	public void setKeynote(Integer keynote) {
		this.keynote = keynote;
	}

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public String getOriginalFile() {
		return originalFile;
	}

	public void setOriginalFile(String originalFile) {
		this.originalFile = originalFile;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "MusicPart [filePath=" + filePath + ", originalFile=" + originalFile + ", keynote=" + keynote
				+ ", tempo=" + tempo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicPart other = (MusicPart) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		return true;
	}

	public File getFile() throws FileNotFoundException {
		File file = new File(this.filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		return file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getKey_right() {
		return key_right;
	}

	public void setKey_right(String key_right) {
		this.key_right = key_right;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
}
