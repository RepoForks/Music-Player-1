package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistsQueryAsyncTask extends AsyncTaskWithProgressBar {

    public interface Callback {
        public void queryCompleted(Map<String, Long> namesToIds, List<String> names);
    }

    private Callback mCallback;
    private Map<String, Long> mNamesToIds;
    private List<String> mNames;
    private long[] mTracksIds;

    public PlaylistsQueryAsyncTask(Fragment fragment, long[] tracksIds, Callback callback) {
        super(fragment);
        mTracksIds = tracksIds;
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();
        queryPlaylists();
        return null;
    }

    private void queryPlaylists() {
        Context context = mFragmentWrapper.getActivity();
        if(context == null) return;

        String[] cols = new String[] {
                AudioStorage.Playlist.PLAYLIST_ID,
                AudioStorage.Playlist.PLAYLIST
        };

        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cols, null, null, null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int capacity = cursor.getCount();
                mNames = new ArrayList<>(capacity);
                mNamesToIds = new HashMap<>(capacity);

                int idIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST);

                String name = null;
                while (!cursor.isAfterLast()) {
                    name = cursor.getString(nameIdx);
                    mNames.add(name);
                    mNamesToIds.put(name, cursor.getLong(idIdx));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
//        ChoosePlaylistDialog dialog =
//                new ChoosePlaylistDialog(mFragmentWrapper.getFragment(),
//                        mFetchedPlaylists, mTracksIds);
//        dialog.show();
        mCallback.queryCompleted(mNamesToIds, mNames);
    }
}
