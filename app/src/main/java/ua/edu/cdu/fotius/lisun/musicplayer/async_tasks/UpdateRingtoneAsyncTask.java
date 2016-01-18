package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class UpdateRingtoneAsyncTask extends AsyncTaskWithProgressBar {

    private Uri mUri;
    private String mNotificationMessage;
    private long mTrackId;
    private Context mContext;

    public UpdateRingtoneAsyncTask(Fragment fragment, Uri uri, long tracksId) {
        super(fragment);
        mUri = uri;
        mTrackId = tracksId;
        mContext = fragment.getActivity();
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

        updateIsRingtoneField();
        mNotificationMessage = getNotificationMessage();

        return null;
    }

    private void updateIsRingtoneField() {
        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.Track.IS_RINGTONE, 1);
        mContext.getContentResolver().update(mUri, values, null, null);
    }

    private String getNotificationMessage() {
        String[] cols = new String[]{
                AudioStorage.Track.TRACK_ID,
                AudioStorage.Track.TRACK
        };

        String whereClause = AudioStorage.Track.TRACK_ID + "=" + mTrackId;
        Cursor cursor = mContext.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cols, whereClause, null, null);

        String message = null;
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                int columnIdxTrackName = cursor.getColumnIndexOrThrow(AudioStorage.Track.TRACK);
                message = mContext.getResources()
                        .getString(R.string.ringtone_set, cursor.getString(columnIdxTrackName));
            }
        }
        return message;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Toast.makeText(mContext, mNotificationMessage, Toast.LENGTH_SHORT).show();
    }
}
