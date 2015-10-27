package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
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
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsBrowserFragment;

public class TrackBrowserFragment extends BaseFragment implements ServiceConnectionObserver {
    public static final String TAG = "tracks";

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private ToolbarStateListener mToolbarStateListener;

    private AbstractCursorLoaderFactory mLoaderFactory;
    private AbstractFragmentContentFactory mContentFactory;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        long albumId, playlistId;
        playlistId = albumId = PARENT_ID_IS_NOT_SET;
        if (args != null) {
            albumId = args.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY, PARENT_ID_IS_NOT_SET);
            playlistId = args.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        }

        Log.d(TAG, "AlbumID: " + albumId);
        Log.d(TAG, "PlaylistID: " + playlistId);

        initFactories(albumId, playlistId);

        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    private void initFactories(long albumId, long playlistId) {
        Log.d(TAG, "AlbumID: " + albumId);
        if(albumId != PARENT_ID_IS_NOT_SET) {
            mLoaderFactory = new AlbumTracksCursorLoaderFactory(getActivity(), albumId);
            mContentFactory =
                    new TracksFragmentContenFactory(getActivity(), mServiceWrapper,
                            mCursorAdapter, mLoaderFactory.getTrackIdColumnName());
        } else if(playlistId != PARENT_ID_IS_NOT_SET) {
            mLoaderFactory = new PlaylistTracksCursorLoaderFactory(getActivity(), playlistId);
            mContentFactory = new PlaylistTracksFragmentContentFactory(getActivity(),
                    mServiceWrapper, mCursorAdapter);
        } else {
            mLoaderFactory = new AllTracksCursorLoaderFactory(getActivity());
            mContentFactory = new TracksFragmentContenFactory(getActivity(), mServiceWrapper,
                    mCursorAdapter, mLoaderFactory.getTrackIdColumnName());
        }
    }

    @Override
    protected CursorAdapter initAdapter() {
        String[] from = new String[]{mLoaderFactory.getTrackColumnName(),
                mLoaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(mContentFactory.getLayoutID(), container, false);
        ListView listView = (ListView) v.findViewById(mContentFactory.getListViewResourceID());
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(mContentFactory.getOnItemClickListener());
        listView.setChoiceMode(mContentFactory.getChoiceMode());
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, mContentFactory.getActionBarMenuContent()));
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
         return mLoaderFactory.getCursorLoader();
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
