package ua.edu.cdu.fotius.lisun.musicplayer.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ListenLog extends RealmObject{

    public static String TRACK_ID_ATTR = "trackId";
    public static String GENRE_ATTR = "genre";
    public static String ARTIST_ATTR = "artist";
    public static String ALBUM_ATTR = "listenedCounter";
    public static String LISTENED_COUNTER_ATTR = "listenedCounter";


    @PrimaryKey
    private long trackId;
    private String genre;
    private String artist;
    private String album;
    private long listenedCounter;

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public long getListenedCounter() {
        return listenedCounter;
    }

    public void setListenedCounter(long listenedCounter) {
        this.listenedCounter = listenedCounter;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
