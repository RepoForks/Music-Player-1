package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

//TODO: maybe inherit from AbstractCursorLoaderCreator(also rename it)
public class EditInfoQueryCreator {

    private long mTrackId = AudioStorage.WRONG_ID;

    public EditInfoQueryCreator(long trackId) {
        mTrackId = trackId;
    }

    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    //uri, projection, selection, selectionArgs, orderBy
    public String[] getProjection() {
        return new String[] {
                getTitleColumnName(),
                getAlbumColumnName(),
                getArtistColumnName(),
                getYearColumnName()
        };
    }

    public String getSelection() {
        return AudioStorage.Track.TRACK_ID + "=?";
    }

    public String[] getSelectionArgs() {
        return new String[] {
            Long.toString(mTrackId)
        };
    }

    public String getSortOrder() {
        return null;
    }

    public String getTitleColumnName() {
        return AudioStorage.Track.TRACK;
    }

    public String getAlbumColumnName() {
        return AudioStorage.Track.ALBUM;
    }

    public String getArtistColumnName() {
        return AudioStorage.Track.ARTIST;
    }

    public String getYearColumnName() {
        return AudioStorage.Track.YEAR;
    }
}
