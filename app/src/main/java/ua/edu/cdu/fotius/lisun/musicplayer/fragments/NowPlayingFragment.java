package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.NowPlayingTrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.NowPlayingCursorLoaderCreator;

public class NowPlayingFragment extends BaseListFragment {
    public static String TAG = "now_playing_tag";

    public NowPlayingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(MediaPlaybackService.QUEUE_CHANGED);
        getActivity().registerReceiver(mQueueChangedReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mQueueChangedReceiver);
    }

    @Override
    protected IndicatorCursorAdapter createCursorAdapter() {
        NowPlayingCursorLoaderCreator loaderCreator = (NowPlayingCursorLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderCreator.getTrackColumnName(),
                loaderCreator.getAlbumColumnName()};
        int[] to = new int[]{R.id.track_title, R.id.track_details};

        return new QueueCursorAdapter(getActivity(), R.layout.row_drag_n_drop_list, from, to,
                loaderCreator.getTrackIdColumnName());
    }

    @Override
    protected AbstractCursorLoaderCreator createCursorLoaderCreator() {
        NowPlayingCursorLoaderCreator creator = new NowPlayingCursorLoaderCreator(getActivity());
        creator.setCurrentQueue(mServiceWrapper.getQueue());
        return creator;
    }

    //TODO: maybe move to BaseLoaderFragment
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderCreator.createCursorLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dragndrop_list, container, false);
        DragNDropListView listView = (DragNDropListView) v.findViewById(R.id.list);
        listView.setDragHandlerResourceID(R.id.handler);
        listView.setDropListener(new OnDropNowPlayingListener(mServiceWrapper));
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        NowPlayingCursorLoaderCreator loaderCreator = (NowPlayingCursorLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView,
                new NowPlayingTrackMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getTrackIdColumnName()));
        return v;
    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        AbstractTracksCursorLoaderCreator loaderFactory =
                (AbstractTracksCursorLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumnName());
    }

    @Override
    protected void setIndicator(PlaybackServiceWrapper serviceWrapper, IndicatorCursorAdapter adapter,
                                AbstractCursorLoaderCreator loaderCreator) {
        NowPlayingCursorLoaderCreator creator = (NowPlayingCursorLoaderCreator)loaderCreator;
        adapter.setIndicatorFor(creator.getTrackIdColumnName(), serviceWrapper.getTrackID());
    }

    private BroadcastReceiver mQueueChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NowPlayingCursorLoaderCreator creator = (NowPlayingCursorLoaderCreator) mLoaderCreator;
            Log.d(TAG, "mService.getQueue == null" + (mServiceWrapper.getQueue() == null));
            creator.setCurrentQueue(mServiceWrapper.getQueue());
            restartLoader();
        }
    };
}
