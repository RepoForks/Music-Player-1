package ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "GetLyricResult", strict = false)
public class LyricsResponse implements Serializable {

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

    public static class StateSaver implements Parcelable {

        private LyricsResponse data;

        public StateSaver(LyricsResponse data) {
            this.data = data;
        }

        private StateSaver(Parcel in) {
            data = new LyricsResponse();
            data.setArtist(in.readString());
            data.setSong(in.readString());
            data.setLyrics(in.readString());
        }

        public static final Creator<StateSaver> CREATOR = new Creator<StateSaver>() {
            @Override
            public StateSaver createFromParcel(Parcel in) {
                return new StateSaver(in);
            }

            @Override
            public StateSaver[] newArray(int size) {
                return new StateSaver[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(data.getArtist());
            dest.writeString(data.getSong());
            dest.writeString(data.getLyrics());
        }

        public LyricsResponse getData() {
            return data;
        }
    }
}
