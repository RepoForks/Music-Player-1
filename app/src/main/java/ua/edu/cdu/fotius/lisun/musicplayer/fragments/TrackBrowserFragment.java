package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.TrackSimpleCursorAdapter;

/**
 * A simple {@link ListFragment} subclass.
 *
 */
public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "track_browser_fragment";
    public static final String ARTIST_TITLE_COLUMN = MediaStore.Audio.Media.ARTIST;
    private final String TRACK_TITLE_COLUMN = MediaStore.Audio.Media.TITLE;
    private final String TRACKS_CURSOR_SORT_ORDER = TRACK_TITLE_COLUMN + " ASC";
    private final int TRACK_LOADER_ID = 1;

    private BaseSimpleCursorAdapter mCursorAdapter;
    private MediaPlaybackServiceWrapper mServiceWrapper;
    private long mAlbumId = -1;

    public TrackBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()---");

        /*do we actually need this if we use Loader?
        * definitely, because don't need to create
        * adapter and call onLoadFinished()
        * everytime on config changes*/
        setRetainInstance(true);

        /*if called with album id
        * then need to list tracks for
        * concrete album*/
         Bundle args = getArguments();
         if(args != null) {
            mAlbumId =
                    args.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY);
         }

        mCursorAdapter = getCursorAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(TRACK_LOADER_ID, null, this);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindService(getActivity());
    }

    private BaseSimpleCursorAdapter getCursorAdapter() {
        String[] from = new String[] { TRACK_TITLE_COLUMN, ARTIST_TITLE_COLUMN };
        int[] to = new int[] { R.id.track_title, R.id.artist_name};

        return new TrackSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mServiceWrapper.playAll(mCursorAdapter.getCursor(), position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks_browser, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindService(getActivity());
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                TRACK_TITLE_COLUMN,
                ARTIST_TITLE_COLUMN
        };

        Log.d(TAG, "onCreateLoader()---");

        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                (mAlbumId == -1) ? null : MediaStore.Audio.Media.ALBUM_ID + " = ?",
                (mAlbumId == -1) ? null : new String[]{Long.toString(mAlbumId)},
                TRACKS_CURSOR_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(TAG, "onLoadFinished()---");

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
