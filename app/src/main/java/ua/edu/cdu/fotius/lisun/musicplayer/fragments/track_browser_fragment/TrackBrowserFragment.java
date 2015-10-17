package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenu;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.UsersPlaylistsBrowserFragment;

public class TrackBrowserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnectionObserver {

    public static final String TAG = "tracks";
    public static final int ID_NOT_SET = -1;
    private final int TRACK_LOADER_ID = 1;

    private BaseSimpleCursorAdapter mCursorAdapter;
    private MediaPlaybackServiceWrapper mServiceWrapper;
    private BaseTrackCursorLoaderFactory mCursorLoaderFactory;
    private ToolbarStateListener mToolbarStateListener;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "-->onCreate()");

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
        long albumId, playlistId;
        albumId = playlistId = ID_NOT_SET;
        if (args != null) {
            albumId = args.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY, ID_NOT_SET);
            playlistId = args.getLong(UsersPlaylistsBrowserFragment.PLAYLIST_ID_KEY, ID_NOT_SET);
        }

        mCursorLoaderFactory = getCursorLoaderFactory(albumId, playlistId);

        mCursorAdapter = getCursorAdapter();
        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(TRACK_LOADER_ID, null, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        TrackMenu trackMenu = new TrackMenu(getActivity(), mServiceWrapper);
        Toolbar toolbar = new Toolbar(getActivity());
        listView.setMultiChoiceModeListener(new MultiChoiceListener(mToolbarStateListener, listView, trackMenu));
    }

    private BaseTrackCursorLoaderFactory getCursorLoaderFactory(long albumId, long playlistId) {
        if(albumId != ID_NOT_SET) {
            return new AlbumTracksCursorLoaderFactory(getActivity(), albumId);
        } else if(playlistId != ID_NOT_SET) {
            return new PlaylistTracksCursorLoaderFactory(getActivity(), playlistId);
        } else {
            return new AllTracksCursorLoaderFactory(getActivity());
        }
    }

    private BaseSimpleCursorAdapter getCursorAdapter() {
        String[] from = new String[]{mCursorLoaderFactory.getTrackColumnName(),
                mCursorLoaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mServiceWrapper.playAll(mCursorAdapter.getCursor(), position,
                mCursorLoaderFactory.getTrackIdColumnName());
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
         return mCursorLoaderFactory.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
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
