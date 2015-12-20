package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class ChangeOrderInPlaylistAsyncTask extends AsyncTask<Void, Void, Void>{

    private HashMap<Long, Integer> mOldIdToPositionMap;
    private long[] mNewPlaylistOrder;
    private ContentResolver mContentResolver;
    private Uri mUri;
    private String mSelection;

    public ChangeOrderInPlaylistAsyncTask(Context context, long playlistID, HashMap<Long, Integer> oldIdToPositionMap,
                                          long[] newPlaylistOrder) {
        mContentResolver = context.getContentResolver();

        mNewPlaylistOrder = newPlaylistOrder;
        mOldIdToPositionMap = oldIdToPositionMap;

        mUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        mSelection = AudioStorage.PlaylistMember.TRACK_ID + "=?" ;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(int i = 0; i < mNewPlaylistOrder.length; i++) {
            long id = mNewPlaylistOrder[i];
            int order = i;
            if(mOldIdToPositionMap.get(id) != order) {
                updateTrackOrder(id, order);
            }
        }
        return null;
    }

    private void updateTrackOrder(long id, int newOrder) {
        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.PlaylistMember.PLAY_ORDER, newOrder);
        DatabaseUtils.queryParamsInLog(mUri, null, mSelection, new String[]{Long.toString(id)});
        mContentResolver.update(mUri, values, mSelection, new String[]{Long.toString(id)});
    }
}
