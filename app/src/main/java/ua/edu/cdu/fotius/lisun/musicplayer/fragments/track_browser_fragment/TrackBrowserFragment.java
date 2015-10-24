package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
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
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsBrowserFragment;

public class TrackBrowserFragment extends BaseFragment implements ServiceConnectionObserver {

    public static final String TAG = "tracks";

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
        /*if called with album id
        * then need to list tracks for
        * concrete album*/
        Bundle args = getArguments();
        long albumId, playlistId;
        albumId = playlistId = PARENT_ID_IS_NOT_SET;
        if (args != null) {
            albumId = args.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY, PARENT_ID_IS_NOT_SET);
            playlistId = args.getLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        }
        mCursorLoaderFactory = getCursorLoaderFactory(albumId, playlistId);

        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    protected CursorAdapter initAdapter() {
        String[] from = new String[]{mCursorLoaderFactory.getTrackColumnName(),
                mCursorLoaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(),
                R.layout.row_tracks_list, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new OnTrackClick(getActivity(), mCursorAdapter, mServiceWrapper,
                mCursorLoaderFactory.getTrackIdColumnName()));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        TrackMenu trackMenu = new TrackMenu(getActivity(), mServiceWrapper);
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), mToolbarStateListener, listView, trackMenu));
        return v;
    }

    private BaseTrackCursorLoaderFactory getCursorLoaderFactory(long albumId, long playlistId) {
        if(albumId != PARENT_ID_IS_NOT_SET) {
            return new AlbumTracksCursorLoaderFactory(getActivity(), albumId);
        } else if(playlistId != PARENT_ID_IS_NOT_SET) {
            return new PlaylistTracksCursorLoaderFactory(getActivity(), playlistId);
        } else {
            return new AllTracksCursorLoaderFactory(getActivity());
        }
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
    public void ServiceConnected() {
        //TODO
    }

    @Override
    public void ServiceDisconnected() {
        //TODO
    }
}
