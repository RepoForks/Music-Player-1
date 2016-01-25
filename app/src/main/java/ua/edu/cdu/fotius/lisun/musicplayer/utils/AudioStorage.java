package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.content.Context;
import android.provider.MediaStore;

public class AudioStorage {

    public static final long WRONG_ID = -1;

    public interface Album {
        public static final String ALBUM_ID = MediaStore.Audio.Albums._ID;
        public static final String ALBUM = MediaStore.Audio.Albums.ALBUM;
        /*There is no ARTIST_ID in MediaStore.Audio.Albums
        * although in database "album_info" view this attribute is present
        * Since MediaStore.Audio.Media.ARTIST_ID also named "artist_id"
        * just use it here. Since "album_info" is a view from "audio" table(with selected "artist_id" from it)
        * this can take place.*/
        public static final String ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;
        public static final String ARTIST = MediaStore.Audio.Albums.ARTIST;
        public static final String ALBUM_ART = MediaStore.Audio.Albums.ALBUM_ART;
        public static final String SORT_ORDER = ALBUM + " ASC";
    }

    public interface Artist {
        public static final String ARTIST_ID = MediaStore.Audio.Artists._ID;
        public static final String ARTIST = MediaStore.Audio.Artists.ARTIST;
        public static final String ALBUMS_QUANTITY = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
        public static final String TRACKS_QUANTITY = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
        public final String SORT_ORDER = ARTIST + " ASC";
    }

    public interface Track {
        public static final String TRACK_ID = MediaStore.Audio.Media._ID;
        public static final String TRACK = MediaStore.Audio.Media.TITLE;
        public static final String ARTIST = MediaStore.Audio.Media.ARTIST;
        public static final String ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;
        public static final String ALBUM = MediaStore.Audio.Media.ALBUM;
        public static final String ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
        public static final String YEAR = MediaStore.Audio.Media.YEAR;
        public static final String FILE_PATH = MediaStore.Audio.Media.DATA;
        public static final String IS_MUSIC = MediaStore.Audio.Media.IS_MUSIC;
        public static final String IS_PODCAST = MediaStore.Audio.Media.IS_PODCAST;
        public static final String IS_RINGTONE = MediaStore.Audio.Media.IS_RINGTONE;
        public static final String SORT_ORDER = TRACK + " ASC";
    }

    public interface Playlist {
        public static final String PLAYLIST_ID = MediaStore.Audio.Playlists._ID;
        public static final String PLAYLIST = MediaStore.Audio.Playlists.NAME;
        public static final String SORT_ORDER = PLAYLIST + " ASC";
    }

    public interface PlaylistMember {
        public static final String PRIMARY_ID = MediaStore.Audio.Playlists.Members._ID;
        public static final String TRACK_ID = MediaStore.Audio.Playlists.Members.AUDIO_ID;
        public static final String TRACK = MediaStore.Audio.Playlists.Members.TITLE;
        public static final String ARTIST = MediaStore.Audio.Playlists.Members.ARTIST;
        public static final String ALBUM_ID = MediaStore.Audio.Playlists.Members.ALBUM_ID;
        public static final String PLAY_ORDER = MediaStore.Audio.Playlists.Members.PLAY_ORDER;
        public static final String SORT_ORDER = PLAY_ORDER + " ASC";
    }

    public interface Genres {
        public static final String GENRE_ID = MediaStore.Audio.Genres._ID;
        public static final String GENRE = MediaStore.Audio.Genres.NAME;
    }

    public interface GenreMember {
        public static final String PRIMARY_ID = MediaStore.Audio.Genres.Members._ID;
        public static final String TRACK_ID = MediaStore.Audio.Genres.Members.AUDIO_ID;
        public static final String TRACK = MediaStore.Audio.Genres.Members.TITLE;
        public static final String ARTIST = MediaStore.Audio.Genres.Members.ARTIST;
        public static final String ALBUM_ID = MediaStore.Audio.Genres.Members.ALBUM_ID;
        public static final String SORT_ORDER = TRACK + " ASC";
    }
}
