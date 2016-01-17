package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class NowPlayingCursorLoaderCreator extends AbstractTracksCursorLoaderCreator {
    private long[] mQueue;

    public NowPlayingCursorLoaderCreator(Context context) {
        super(context);
    }

    public void setCurrentQueue(long[] queue) {
        mQueue = queue;
    }

    @Override
    public int getLoaderId() {
        return NOW_PLAYING_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getTrackIdColumnName(),
                getTrackColumnName(),
                getAlbumColumnName()
        };
    }

    @Override
    public String getSelection() {
        return (getTrackIdColumnName() + " IN (" + DatabaseUtils.makeInBody(mQueue) + ")");
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }



    @Override
    public String getSortOrder() {
        if (isAbleToFormQuery()) {
            return ("CASE " + getTrackIdColumnName() + makeCaseBody());
        }
        return ("");
    }

    private boolean isAbleToFormQuery() {
        return ((mQueue != null) && (mQueue.length > 0));
    }

    private String makeCaseBody() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mQueue.length; i++) {
            stringBuffer.append(" when " + mQueue[i] +
                    " then " + i);
        }
        stringBuffer.append(" end");
        return stringBuffer.toString();
    }

    @Override
    public String getTrackIdColumnName() {
        return AudioStorage.Track.TRACK_ID;
    }

    @Override
    public String getTrackColumnName() {
        return AudioStorage.Track.TRACK;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.Track.ARTIST;
    }

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.Track.ALBUM_ID;
    }

    public String getAlbumColumnName() {
        return AudioStorage.Track.ALBUM;
    }
}
