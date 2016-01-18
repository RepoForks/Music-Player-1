package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class QueueLoaderCreator extends BaseTracksLoaderCreator {
    private long[] mQueue;

    public QueueLoaderCreator(Context context) {
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
                getTrackIdColumn(),
                getTrackColumn(),
                getAlbumColumn()
        };
    }

    @Override
    public String getSelection() {
        return (getTrackIdColumn() + " IN (" + DatabaseUtils.makeInBody(mQueue) + ")");
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }



    @Override
    public String getSortOrder() {
        if (isAbleToFormQuery()) {
            return ("CASE " + getTrackIdColumn() + makeCaseBody());
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

    public String getAlbumColumn() {
        return AudioStorage.Track.ALBUM;
    }
}
