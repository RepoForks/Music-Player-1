package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.TrackDetalizationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.TrackBrowserFragment;

public class UsersPlaylistsBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "playlists";
    public static final String PLAYLIST_ID_KEY = "playlist_id_key";
    private final int USERS_PLAYLISTS_LOADER = 1;

    private BaseSimpleCursorAdapter mAdapter;

    public UsersPlaylistsBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mAdapter = getAdapter();
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(USERS_PLAYLISTS_LOADER, null, this);
    }

    private BaseSimpleCursorAdapter getAdapter() {
        String[] from = new String[] { AudioStorage.UserPlaylist.PLAYLIST };
        int[] to = new int[] { R.id.playlist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_user_playlists_list, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            //TODO: to separate method with AlbumBrowserFragment's
            int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.UserPlaylist.PLAYLIST_ID);
            long playlistId = cursor.getLong(idColumnIndex);
            Bundle bundle = new Bundle();
            bundle.putLong(PLAYLIST_ID_KEY, playlistId);
            Intent intent = new Intent(getActivity(), TrackDetalizationActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                AudioStorage.UserPlaylist.PLAYLIST_ID,
                AudioStorage.UserPlaylist.PLAYLIST
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                projection, null, null, AudioStorage.UserPlaylist.SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
