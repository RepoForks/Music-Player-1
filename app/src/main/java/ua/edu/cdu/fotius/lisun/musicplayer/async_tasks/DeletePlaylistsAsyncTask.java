package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class DeletePlaylistsAsyncTask extends AsyncTaskWithProgressBar {

    private final String TAG = getClass().getSimpleName();

    private long[] mIds;

    public DeletePlaylistsAsyncTask(Fragment fragment, long[] ids) {
        super(fragment);
        mIds = ids;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);
        Context context = mFragmentWrapper.getActivity();
        if (context == null) return null;

        String whereClause = getWhereClause();
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, whereClause, null);
        return null;
    }

    //TODO: move to utils with DeleteTracks
    private String getWhereClause() {
        StringBuffer whereClause = new StringBuffer();
        whereClause.append(AudioStorage.Playlist.PLAYLIST_ID + " IN (");
        for (int i = 0; i < mIds.length; i++) {
            whereClause.append(mIds[i]);
            if (i < mIds.length - 1) {
                whereClause.append(",");
            }
        }
        whereClause.append(")");
        return whereClause.toString();
    }
}
