package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.ChoosePlaylistDialog;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameIdTuple;

public class PlaylistsQueryAsyncTask extends AsyncTaskWithProgressBar {

    private ArrayList<PlaylistNameIdTuple> mFetchedPlaylists;
    private long[] mTracksIds;

    public PlaylistsQueryAsyncTask(Fragment fragment, long[] tracksIds) {
        super(fragment);
        mTracksIds = tracksIds;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();
        mFetchedPlaylists = getListItems();
        return null;
    }

    private ArrayList<PlaylistNameIdTuple> getListItems() {
        Context context = mFragmentWrapper.getActivity();
        if(context == null) return null;

        String[] cols = new String[] {
                AudioStorage.Playlist.PLAYLIST_ID,
                AudioStorage.Playlist.PLAYLIST
        };

        ArrayList<PlaylistNameIdTuple> playlists = new ArrayList<PlaylistNameIdTuple>();
        //first in the list is "New" item
        String createNewText = context.getResources().getString(R.string.create_new_playlist_text);
        playlists.add(new PlaylistNameIdTuple(-1, createNewText));

        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cols, null, null, null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST);
                while (!cursor.isAfterLast()) {
                    playlists.add(new PlaylistNameIdTuple(cursor.getLong(idIdx),
                            cursor.getString(nameIdx)));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return playlists;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ChoosePlaylistDialog dialog =
                new ChoosePlaylistDialog(mFragmentWrapper.getFragment(),
                        mFetchedPlaylists, mTracksIds);
        dialog.show();
    }
}
