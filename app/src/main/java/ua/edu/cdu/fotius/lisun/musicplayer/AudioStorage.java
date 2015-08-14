package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.provider.MediaStore;

public class AudioStorage {

    public interface AlbumBrowser {
        public static final String ALBUM_ID = MediaStore.Audio.Albums._ID;
        public static final String ALBUM = MediaStore.Audio.Albums.ALBUM;
        public static final String ARTIST = MediaStore.Audio.Albums.ARTIST;
        public static final String SORT_ORDER = ALBUM + " ASC";
    }

    public interface ArtistBrowser {
        public static final String ARTIST_ID = MediaStore.Audio.Artists._ID;
        public static final String ARTIST = MediaStore.Audio.Artists.ARTIST;
        public static final String ALBUMS_QUANTITY = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
        public static final String TRACKS_QUANTITY = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
        public final String SORT_ORDER = ARTIST + " ASC";
    }

    public interface TrackBrowser {
        public static final String TRACK_ID = MediaStore.Audio.Media._ID;
        public static final String TRACK = MediaStore.Audio.Media.TITLE;
        public static final String ARTIST = MediaStore.Audio.Media.ARTIST;
        public static final String SORT_ORDER = TRACK + " ASC";
    }
}
