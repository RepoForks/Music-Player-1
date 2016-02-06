package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.RenameDialogBuilder;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistNameAsyncRetriever extends AsyncTaskWithProgressBar {

    private final String TAG = getClass().getSimpleName();

    private long mID;

    public PlaylistNameAsyncRetriever(Fragment fragment, long playlistID) {
        super(fragment);
        mID = playlistID;
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
            ToolbarActivity activity = mFragmentWrapper.getActivity();
            if (activity == null) return;
            LayoutInflater inflater = activity.getLayoutInflater();

            RenameDialogBuilder builder = new RenameDialogBuilder(activity, inflater, playlist);
            builder.create().show();
        }
        super.onPostExecute(obj);
    }
}

