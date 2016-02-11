package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistNameAsyncRetriever extends AsyncTaskWithProgressBar {

    public interface Callback {
        public void queryCompleted(String playlistName);
    }

    private long mID;
    private WeakReference<Callback> mCallbackReference;

    public PlaylistNameAsyncRetriever(Fragment fragment, long playlistID,
                                      Callback callback) {
        super(fragment);
        mID = playlistID;
        mCallbackReference =
                new WeakReference<Callback>(callback);
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

        Context context = mFragmentWrapper.getActivity();
        if (context == null) return null;

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{AudioStorage.Playlist.PLAYLIST};
        String where = AudioStorage.Playlist.PLAYLIST_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(mID)};

        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(uri, projection, where, whereArgs, null);
        String playlist = null;
        if (c != null) {
            if(c.moveToFirst()) {
                playlist = c.getString(0);
            }
            c.close();
        }
        return playlist;
    }

    @Override
    protected void onPostExecute(Object obj) {
        String playlist = (String) obj;
        if (playlist != null) {
            Callback callback = mCallbackReference.get();
            if(callback != null) {
                callback.queryCompleted(playlist);
            }
        }
        super.onPostExecute(obj);
    }
}

