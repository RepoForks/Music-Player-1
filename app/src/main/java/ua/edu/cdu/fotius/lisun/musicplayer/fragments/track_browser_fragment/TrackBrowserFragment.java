package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenu;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenu;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AbstractCursorLoaderFactory;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumArtCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseLoaderFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;

public class TrackBrowserFragment extends BaseLoaderFragment implements ServiceConnectionObserver {

    public static final String TAG = "tracks";

    private MediaPlaybackServiceWrapper mServiceWrapper;
    protected ToolbarStateListener mToolbarStateListener;

    private Bundle mExtras;

    public TrackBrowserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToolbarStateListener = (ToolbarStateListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mExtras = getArguments();

        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    protected CursorAdapter createAdapter() {
        TracksCursorLoaderFactory loaderFactory = (TracksCursorLoaderFactory) mLoaderFactory;
        String[] from = new String[]{loaderFactory.getTrackColumnName(),
                loaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new AlbumArtCursorAdapter(getActivity(), R.layout.row_tracks_list, from, to, R.id.album_art, loaderFactory.getAlbumIdColumnName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tracks_browser, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new TrackMenu(getActivity(), mServiceWrapper)));
        return v;
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        Bundle extras = mExtras;
        if (extras == null) {
            return new AllTracksCursorLoaderFactory(getActivity());
        }
        long artistId = extras.getLong(ArtistsBrowserFragment.ARTIST_ID_KEY, PARENT_ID_IS_NOT_SET);
        long albumId = extras.getLong(AlbumsBrowserFragment.ALBUM_ID_KEY, PARENT_ID_IS_NOT_SET);
        if ((artistId != PARENT_ID_IS_NOT_SET) && (albumId != PARENT_ID_IS_NOT_SET)) {
            return new ArtistAlbumTracksCursorLoaderFactory(getActivity(), artistId, albumId);
        } else if (albumId != PARENT_ID_IS_NOT_SET) {
            return new AlbumTracksCursorLoaderFactory(getActivity(), albumId);
        } else {
            /*this won't be executed, but keep this as "default value"*/
            return new AllTracksCursorLoaderFactory(getActivity());
        }

    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        TracksCursorLoaderFactory loaderFactory =
                (TracksCursorLoaderFactory) mLoaderFactory;
        return new OnTrackClick(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumnName());
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
