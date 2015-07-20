package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link ListFragment} subclass.
 *
 */
public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TRACK_TITLE_COLUMN = MediaStore.Audio.Media.TITLE;
    private final String ARTIST_TITLE_COLUMN = MediaStore.Audio.Media.ARTIST;
    private final String CURSOR_SORT_ORDER = TRACK_TITLE_COLUMN + " ASC";
    private final int TRACK_LOADER_ID = 1;
    private final String TAG = getClass().getSimpleName();

    private SimpleCursorAdapter mCursorAdapter;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public TrackBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //do we actually need this if we use Loader?
        //definitely, because don't need to create
        //adapter and call onLoadFinished()
        //everytime on config changes
        setRetainInstance(true);
        
        mCursorAdapter = (SimpleCursorAdapter)getCursorAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(TRACK_LOADER_ID, null, this);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindService(getActivity());
    }

    private CursorAdapter getCursorAdapter() {
        String[] from = new String[] { TRACK_TITLE_COLUMN, ARTIST_TITLE_COLUMN };
        int[] to = new int[] { R.id.song_title, R.id.artist_title };

        return new SimpleCursorAdapter(getActivity(),
                R.layout.row_songs_list, /*cursor*/null,
                from, to, /*don't observe changes*/0);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mServiceWrapper.playAll(mCursorAdapter.getCursor(), position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_browser, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindService(getActivity());
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String[] mProjection = new String[] {
                MediaStore.Audio.Media._ID,
                TRACK_TITLE_COLUMN,
                ARTIST_TITLE_COLUMN
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mProjection, null, null, CURSOR_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case TRACK_LOADER_ID:
                mCursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursorAdapter.swapCursor(null);
    }
}
