package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


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
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;

public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver{

    public static final String TAG = "track_browser_fragment";
    private final int TRACK_LOADER_ID = 1;

    private BaseSimpleCursorAdapter mCursorAdapter;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    private long mAlbumId = -1;

    public TrackBrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*do we actually need this if we use Loader?
        * definitely, because don't need to create
        * adapter and call onLoadFinished()
        * everytime on config changes*/
        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);

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
        String[] from = new String[] { AudioStorage.TrackBrowser.TRACK,
                AudioStorage.TrackBrowser.ARTIST };
        int[] to = new int[] { R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
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
        mServiceWrapper.unbindFromService(getActivity(), this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                AudioStorage.TrackBrowser.TRACK_ID,
                AudioStorage.TrackBrowser.TRACK,
                AudioStorage.TrackBrowser.ARTIST
        };
        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                (mAlbumId == -1) ? null : MediaStore.Audio.Media.ALBUM_ID + " = ?",
                (mAlbumId == -1) ? null : new String[]{Long.toString(mAlbumId)},
                AudioStorage.TrackBrowser.SORT_ORDER);
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
