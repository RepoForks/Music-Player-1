package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistNameAsyncUpdater extends AsyncTaskWithProgressBar{

    private final String TAG = getClass().getSimpleName();

    private String mOldName;
    private String mNewName;
    private long mId;

    public PlaylistNameAsyncUpdater(Fragment fragment, long playlistId, String oldName, String newName) {
        super(fragment);
        mOldName = oldName;
        mNewName = newName;
        mId = playlistId;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

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
        Context context = mFragmentWrapper.getActivity();
        if (context == null) return;
        String message =
                context.getResources().getString(R.string.rename_dialog_result_message, mOldName, mNewName);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        super.onPostExecute(o);
    }
}
