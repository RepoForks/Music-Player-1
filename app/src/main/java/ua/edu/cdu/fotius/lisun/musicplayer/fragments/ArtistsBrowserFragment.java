package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import ua.edu.cdu.fotius.lisun.musicplayer.OnFragmentReplaceListener;

public class ArtistsBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "artists_browser_fragment";
    public static final String ARTIST_ID_KEY = "artist_id_key";
    private final int ARTISTS_LOADER = 1;

    private OnFragmentReplaceListener mFragmentReplaceCallback;
    private BaseSimpleCursorAdapter mCursorAdapter;

    public ArtistsBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mCursorAdapter = getAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(ARTISTS_LOADER, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentReplaceCallback = (OnFragmentReplaceListener) activity;
    }

    private BaseSimpleCursorAdapter getAdapter() {

        String[] from = new String[]{AudioStorage.ArtistBrowser.ARTIST, AudioStorage.ArtistBrowser.ALBUMS_QUANTITY,
                AudioStorage.ArtistBrowser.TRACKS_QUANTITY};
        int[] to = new int[]{R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_artist_list, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artists_browser, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                AudioStorage.ArtistBrowser.ARTIST_ID,
                AudioStorage.ArtistBrowser.ARTIST,
                AudioStorage.ArtistBrowser.ALBUMS_QUANTITY,
                AudioStorage.ArtistBrowser.TRACKS_QUANTITY
        };

        return new CursorLoader(getActivity(), MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection, null, null, AudioStorage.ArtistBrowser.SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    /* On artist item click show fragment with albums of
    * this concrete artist */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if ((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex =
                    cursor.getColumnIndexOrThrow(AudioStorage.ArtistBrowser.ARTIST_ID);
            long artistId = cursor.getLong(idColumnIndex);
            Bundle bundle = new Bundle();
            bundle.putLong(ARTIST_ID_KEY, artistId);
            Fragment fragment = new AlbumsBrowserFragment();
            fragment.setArguments(bundle);
            mFragmentReplaceCallback.replaceFragment(fragment, AlbumsBrowserFragment.TAG);
        }
    }
}
