package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.model.ListenLogWorker;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class DeleteTracksAsyncTask extends AsyncTaskWithProgressBar {

    private long[] mTrackIds;
    private WeakReference<MediaPlaybackServiceWrapper> mServiceWrapperReference;

    public DeleteTracksAsyncTask(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long[] tracksId) {
        super(fragment);
        mTrackIds = tracksId;
        mServiceWrapperReference = new WeakReference<MediaPlaybackServiceWrapper>(serviceWrapper);
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();
        Context context = mFragmentWrapper.getActivity();
        if (context == null) return null;

        String[] cols = new String[]{
                AudioStorage.Track.TRACK_ID, AudioStorage.Track.FILE_PATH
        };
        String whereClause = getWhereClause();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                cols, whereClause, null, null);
        if (cursor != null) {
            deleteFromCurrentPlayQueue(cursor);
            deleteFromStorage(cursor);
            deleteFromDatabase(resolver, whereClause);
            new ListenLogWorker(context).deleteFromLog(mTrackIds);
            cursor.close();
        }

        return null;
    }

    private String getWhereClause() {
        StringBuffer whereClause = new StringBuffer();
        whereClause.append(AudioStorage.Track.TRACK_ID + " IN (");
        for (int i = 0; i < mTrackIds.length; i++) {
            whereClause.append(mTrackIds[i]);
            if (i < mTrackIds.length - 1) {
                whereClause.append(",");
            }
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    private void deleteFromCurrentPlayQueue(Cursor cursor) {
        MediaPlaybackServiceWrapper serviceWrapper = mServiceWrapperReference.get();
        if (serviceWrapper == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long trackId = cursor.getLong(0);
                serviceWrapper.removeTrack(trackId);
                cursor.moveToNext();
            }
        }
    }

    private void deleteFromStorage(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String path = cursor.getString(1);
                File f = new File(path);
                f.delete();
                cursor.moveToNext();
            }
        }
    }

    private void deleteFromDatabase(ContentResolver resolver, String where) {
        resolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, where, null);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        DialogFragment fragment = (DialogFragment) mFragmentWrapper.getFragment();
        if(fragment == null) return;
        fragment.dismiss();
    }

}
