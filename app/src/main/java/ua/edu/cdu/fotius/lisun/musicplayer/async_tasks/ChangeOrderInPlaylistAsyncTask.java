package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ChangeOrderInPlaylistAsyncTask extends AsyncTaskWithProgressBar {

    private HashMap<Long, Integer> mOldIdToPositionMap;
    private long[] mNewPlaylistOrder;
    private Uri mUri;
    private String mSelection;

    public ChangeOrderInPlaylistAsyncTask(Fragment fragment, long playlistID, HashMap<Long, Integer> oldIdToPositionMap,
                                          long[] newPlaylistOrder) {
        super(fragment);
        mNewPlaylistOrder = newPlaylistOrder;
        mOldIdToPositionMap = oldIdToPositionMap;

        mUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        mSelection = AudioStorage.PlaylistMember.TRACK_ID + "=?" ;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();
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
        Context context = mFragmentWrapper.getActivity();
        if(context == null) return;

        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.PlaylistMember.PLAY_ORDER, newOrder);
        context.getContentResolver().update(mUri, values, mSelection, new String[]{Long.toString(id)});
    }
}
