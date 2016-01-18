package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class AllTracksLoaderCreator extends BaseTracksLoaderCreator {

    public AllTracksLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return TRACKS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getTrackIdColumn(),
                getTrackColumn(),
                getArtistColumn(),
                getAlbumIdColumn()
        };
    }

    @Override
    public String getSelection() {
        return getTrackColumn() + " != '' AND " + AudioStorage.Track.IS_MUSIC + " = 1";
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    @Override
    public String getSortOrder() {
        return AudioStorage.Track.SORT_ORDER;
    }

    @Override
    public String getTrackIdColumn() {
        return AudioStorage.Track.TRACK_ID;
    }

    @Override
    public String getTrackColumn() {
        return AudioStorage.Track.TRACK;
    }

    @Override
    public String getArtistColumn() {
        return AudioStorage.Track.ARTIST;
    }

    @Override
    public String getAlbumIdColumn() {
        return AudioStorage.Track.ALBUM_ID;
    }
}
