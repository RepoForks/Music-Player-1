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
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseLoaderFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;

public class TrackBrowserFragment extends BaseLoaderFragment implements ServiceConnectionObserver {

    public static final String TAG = "tracks";

    private MediaPlaybackServiceWrapper mServiceWrapper;
    protected ToolbarStateListener mToolbarStateListener;

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
        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);

        //TODO:
        //need to be after mServiceWrapper init
    }

    @Override
    protected CursorAdapter createAdapter() {
        TracksCursorLoaderFactory loaderFactory = (TracksCursorLoaderFactory) mLoaderFactory;
        String[] from = new String[]{loaderFactory.getTrackColumnName(),
                loaderFactory.getArtistColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.artist_name};

        return new BaseSimpleCursorAdapter(getActivity(), getRowLayoutID(), from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "OnCreateView ---> TracksBrowserFragment");

        View v = inflater.inflate(getLayoutID(), container, false);
        ListView listView = (ListView) v.findViewById(getListViewResourceID());
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(getChoiceMode());
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, createActionBarMenuContent()));
        return v;
    }

    protected int getLayoutID() {
        return R.layout.fragment_tracks_browser;
    }

    protected int getListViewResourceID() {
        return R.id.list;
    }

    protected int getRowLayoutID() {
        return R.layout.row_tracks_list;
    }

    protected int getChoiceMode() {
        return ListView.CHOICE_MODE_MULTIPLE_MODAL;
    }

    @Override
    protected AbstractCursorLoaderFactory createLoaderFactory() {
        return new AllTracksCursorLoaderFactory(getActivity());
    }

    protected BaseMenu createActionBarMenuContent() {
        return new TrackMenu(getActivity(), mServiceWrapper);
    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        TracksCursorLoaderFactory loaderFactory =
                (TracksCursorLoaderFactory) mLoaderFactory;
        return new OnTrackClick(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumnName());
    }

    //TODO: do i actually need this in subclasses?
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
