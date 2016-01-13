package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class NowPlayingCursorLoaderCreator extends AbstractTracksCursorLoaderCreator {

    private long[] mPlayingQueue;

    public NowPlayingCursorLoaderCreator(Context context, long[] playingQueue) {
        super(context);
        mPlayingQueue = playingQueue;
    }

    @Override
    public int getLoaderId() {
        return NOW_PLAYING_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    private boolean isAbleToFormQuery() {
        return ((mPlayingQueue != null) && (mPlayingQueue.length > 0));
    }

    @Override
    public String[] getProjection() {
        String[] projection = null;
        if (isAbleToFormQuery()) {
            projection = new String[]{
                    getTrackIdColumnName(),
                    getTrackColumnName(),
                    getAlbumColumnName()
            };
        }
        return projection;
    }

    @Override
    public String getSelection() {
        String selection = null;
        if (isAbleToFormQuery()) {
            selection = getTrackIdColumnName() + " IN (" + makeInBody() + ")";
        }
        return selection;
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    private String makeInBody() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(mPlayingQueue[0]);
        for (int i = 1; i < mPlayingQueue.length; i++) {
            stringBuffer.append(", " + mPlayingQueue[i]);
        }
        return stringBuffer.toString();
    }

    @Override
    public String getSortOrder() {
        String order = null;
        if (isAbleToFormQuery()) {
            order = "CASE " + getTrackIdColumnName() + makeCaseBody();
        }
        return order;
    }

    private String makeCaseBody() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mPlayingQueue.length; i++) {
            stringBuffer.append(" when " + mPlayingQueue[i] +
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
