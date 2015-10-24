package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.BaseTrackCursorLoaderFactory;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.PlaylistTracksCursorLoaderFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistTracksFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver {

    private final int ID_NOT_SET = -1;
    private final int PLAYLIST_TRACKS_LOADER_ID = 1;

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private BaseTrackCursorLoaderFactory mCursorLoaderFactory;
    private BaseSimpleCursorAdapter mCursorAdapter;

    public PlaylistTracksFragment() {
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
        long playlistId = ID_NOT_SET;
        if (args != null) {
            playlistId = args.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, ID_NOT_SET);
            if(playlistId != ID_NOT_SET) {
                mCursorLoaderFactory = new PlaylistTracksCursorLoaderFactory(getActivity(), playlistId);
            }
        }



        mCursorAdapter = getCursorAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(PLAYLIST_TRACKS_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_tracks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //TODO:
        super.onViewCreated(view, savedInstanceState);
    }

    private BaseSimpleCursorAdapter getCursorAdapter() {
        String[] from = new String[]{mCursorLoaderFactory.getTrackColumnName(),
                mCursorLoaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void ServiceConnected() {

    }

    @Override
    public void ServiceDisconnected() {

    }
}
