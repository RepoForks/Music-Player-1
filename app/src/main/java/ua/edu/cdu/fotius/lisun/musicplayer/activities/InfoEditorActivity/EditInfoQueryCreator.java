package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

//TODO: maybe inherit from AbstractCursorLoaderCreator(also rename it)
public class EditInfoQueryCreator {

    private long mTrackId = AudioStorage.WRONG_ID;
    private String mSelection;
    private String[] mSelectionArgs;

    public EditInfoQueryCreator(long trackId) {
        mTrackId = trackId;
        mSelection = AudioStorage.Track.TRACK_ID + "=?";
        mSelectionArgs = new String[] { Long.toString(mTrackId) };
    }

    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    //uri, projection, selection, selectionArgs, orderBy
    public String[] getProjection() {
        return new String[] {
                getTitleColumn(),
                getAlbumColumn(),
                getArtistColumn(),
                getYearColumn()
        };
    }

    public String getSelection() {
        return mSelection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public String getSortOrder() {
        return null;
    }

    public String getTitleColumn() {
        return AudioStorage.Track.TRACK;
    }

    public String getAlbumColumn() {
        return AudioStorage.Track.ALBUM;
    }

    public String getArtistColumn() {
        return AudioStorage.Track.ARTIST;
    }

    public String getYearColumn() {
        return AudioStorage.Track.YEAR;
    }
}
