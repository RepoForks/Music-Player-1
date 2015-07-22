package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsBrowserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = getClass().getSimpleName();
    private final int ALBUMS_LOADER_ID = 1;

    private final String ALBUM_TITLE_COLUMN = MediaStore.Audio.Albums.ALBUM;
    private final String ARTIST_NAME_COLUMN = MediaStore.Audio.Albums.ARTIST;

    private final String CURSOR_SORT_ORDER = ALBUM_TITLE_COLUMN + " ASC";

    private CursorAdapter mCursorAdapter = null;
    private View mFragmentLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        //do we actually need this if we use Loader?
        //definitely, because don't need to create
        //adapter and call onLoadFinished()
        //everytime on config changes
        setRetainInstance(true);

        getLoaderManager().initLoader(ALBUMS_LOADER_ID, null, this);

        String[] from = new String[] { ALBUM_TITLE_COLUMN, ARTIST_NAME_COLUMN };
        int[] to = new int[] { R.id.album_title, R.id.artist_name };

        mCursorAdapter = new AlbumCursorAdapter(getActivity(),
                R.layout.grid_item_albums, /*cursor*/null,
                from, to);

        /*Move this here to aim some similarity
        of creating fragments. Also don't need
        inflate in onCreateView() when configuration
        changed
        TODO: But maybe it's not good idea to pass null
        */
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mFragmentLayout = inflater.inflate(R.layout.fragment_albums_browser, null, false);
        GridView gridView = (GridView)mFragmentLayout.findViewById(R.id.grid_container);
        gridView.setAdapter(mCursorAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        return mFragmentLayout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = new String[] {
                MediaStore.Audio.Albums._ID,
                ALBUM_TITLE_COLUMN,
                ARTIST_NAME_COLUMN
        };


        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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
}
