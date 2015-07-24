package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.NavigationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.AlbumSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.ArtistSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.OnFragmentReplaceListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = getClass().getSimpleName();

    //TODO: Maybe move this to another class
    public static final String ARTIST_COLUMN_NAME = MediaStore.Audio.Artists.ARTIST;
    public static final String NUMBER_OF_ALBUMS_COLUMN_NAME = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
    public static final String NUMBER_OF_TRACKS_COLUMN_NAME = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
    private static final String ARTIST_ID_COLUMN_NAME = MediaStore.Audio.Artists._ID;
    public static final String ARTIST_ID_KEY = "artist_id";
    private final String CURSOR_SORT_ORDER = ARTIST_COLUMN_NAME + " ASC";

    private final int ARTISTS_LOADER = 1;

    private OnFragmentReplaceListener mCallback;
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
        mCallback = (OnFragmentReplaceListener) activity;
    }

    private BaseSimpleCursorAdapter getAdapter() {
        String[] from = new String[] { ARTIST_COLUMN_NAME, NUMBER_OF_ALBUMS_COLUMN_NAME,
                NUMBER_OF_TRACKS_COLUMN_NAME };
        int[] to = new int[] { R.id.artist_name, R.id.albums_quantity, R.id.tracks_quantity };

        return new ArtistSimpleCursorAdapter(getActivity(),
                R.layout.row_artist_list, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artists_browser, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = new String[] {
            ARTIST_ID_COLUMN_NAME,
            ARTIST_COLUMN_NAME,
            NUMBER_OF_ALBUMS_COLUMN_NAME,
            NUMBER_OF_TRACKS_COLUMN_NAME
        };

        //Log.d(TAG, "Content Uri: " + MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);

        return new CursorLoader(getActivity(), MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection, null, null, CURSOR_SORT_ORDER);
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
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(ARTIST_ID_COLUMN_NAME);
            long artistId = cursor.getLong(idColumnIndex);
            Bundle bundle = new Bundle();
            bundle.putLong(ARTIST_ID_KEY, artistId);
            Fragment fragment = new AlbumsBrowserFragment();
            fragment.setArguments(bundle);
            mCallback.replaceFragment(fragment, NavigationActivity.ALBUMS_BROWSER_FRAGMENT_TAG);
        }
    }
}
