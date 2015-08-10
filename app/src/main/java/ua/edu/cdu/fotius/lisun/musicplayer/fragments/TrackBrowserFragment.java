package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
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

import ua.edu.cdu.fotius.lisun.musicplayer.ServiceInterface;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.TrackSimpleCursorAdapter;

public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver{

    public static final String TAG = "track_browser_fragment";
    public static final String ARTIST_TITLE_COLUMN = MediaStore.Audio.Media.ARTIST;
    private final String TRACK_TITLE_COLUMN = MediaStore.Audio.Media.TITLE;
    private final String TRACKS_CURSOR_SORT_ORDER = TRACK_TITLE_COLUMN + " ASC";
    private final int TRACK_LOADER_ID = 1;

    private BaseSimpleCursorAdapter mCursorAdapter;
    private ServiceInterface mServiceCallbacks;

    private long mAlbumId = -1;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mServiceCallbacks = (ServiceInterface) activity;
    }

    /*Don't bind/unbind in TrackBrowserFragment, because
        * this fragment restarts every time user choose
        * another fragment in menu, so music playback will
        * stopped*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*do we actually need this if we use Loader?
        * definitely, because don't need to create
        * adapter and call onLoadFinished()
        * everytime on config changes*/
        setRetainInstance(true);

        mServiceCallbacks.bindToService(getActivity(), this);

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
    }

    private BaseSimpleCursorAdapter getCursorAdapter() {
        String[] from = new String[] { TRACK_TITLE_COLUMN, ARTIST_TITLE_COLUMN };
        int[] to = new int[] { R.id.track_title, R.id.artist_name};

        return new TrackSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mServiceCallbacks.playAll(mCursorAdapter.getCursor(), position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks_browser, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceCallbacks.unbindFromService(getActivity(), this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                TRACK_TITLE_COLUMN,
                ARTIST_TITLE_COLUMN
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                (mAlbumId == -1) ? null : MediaStore.Audio.Media.ALBUM_ID + " = ?",
                (mAlbumId == -1) ? null : new String[]{Long.toString(mAlbumId)},
                TRACKS_CURSOR_SORT_ORDER);
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

    @Override
    public void ServiceConnected() {
        //TODO
    }

    @Override
    public void ServiceDisconnected() {
        //TODO
    }
}
