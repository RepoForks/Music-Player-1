package ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetLyricResult", strict = false)
public class LyricsResponse {

    @Element(name = "LyricSong")
    private String song;
    @Element(name = "LyricArtist")
    private String artist;
    @Element(name = "Lyric")
    private String lyrics;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
