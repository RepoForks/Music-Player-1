package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistNameAsyncUpdater extends AsyncTaskWithProgressBar{

    public interface Callback {
        public void updated();
    }

    private String mOldName;
    private String mNewName;
    private long mId;
    private Callback mCallback;

    public PlaylistNameAsyncUpdater(Fragment fragment, long playlistId, Callback callback) {
        super(fragment);
        mId = playlistId;
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

        mOldName = (String)params[0];
        mNewName = (String)params[1];

        Context context = mFragmentWrapper.getActivity();
        if (context == null) return null;

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String where = AudioStorage.Playlist.PLAYLIST_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(mId)};

        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.Playlist.PLAYLIST, mNewName);

        ContentResolver resolver = context.getContentResolver();
        resolver.update(uri, values, where, whereArgs);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        mCallback.updated();
        Context context = mFragmentWrapper.getActivity();
        if (context == null) return;
        String message =
                context.getResources().getString(R.string.rename_dialog_result_message, mOldName, mNewName);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        super.onPostExecute(o);
    }
}
