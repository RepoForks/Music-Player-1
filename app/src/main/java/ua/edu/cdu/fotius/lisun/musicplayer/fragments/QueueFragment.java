package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.QueueCursorAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.QueueTrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.QueueLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDropNowPlayingListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnTrackClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.views.DragNDropListView;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators.BaseTracksLoaderCreator;

public class QueueFragment extends BaseFragment {
    public static String TAG = "now_playing_tag";

    public QueueFragment() {
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
    protected BaseCursorAdapter createCursorAdapter() {
        QueueLoaderCreator loaderCreator = (QueueLoaderCreator) mLoaderCreator;
        String[] from = new String[]{loaderCreator.getTrackColumn(),
                loaderCreator.getAlbumColumn()};
        int[] to = new int[]{R.id.track_title, R.id.track_details};

        return new QueueCursorAdapter(getActivity(), R.layout.row_drag_n_drop_list, from, to,
                loaderCreator.getTrackIdColumn());
    }

    @Override
    protected BaseLoaderCreator createCursorLoaderCreator() {
        QueueLoaderCreator creator = new QueueLoaderCreator(getActivity());
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
        View v = inflater.inflate(R.layout.fragment_now_playing, container, false);
        DragNDropListView listView = (DragNDropListView) v.findViewById(R.id.list);
        listView.setDragHandlerResourceID(R.id.handler);
        listView.setDropListener(new OnDropNowPlayingListener(mServiceWrapper));
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        QueueLoaderCreator loaderCreator = (QueueLoaderCreator) mLoaderCreator;
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(), listView,
                new QueueTrackMenuCommandSet(this, mServiceWrapper),
                loaderCreator.getTrackIdColumn()));
        return v;
    }

    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        BaseTracksLoaderCreator loaderFactory =
                (BaseTracksLoaderCreator) mLoaderCreator;
        return new OnTrackClickListener(getActivity(), mCursorAdapter,
                mServiceWrapper, loaderFactory.getTrackIdColumn());
    }

    @Override
    protected void setIndicator(MediaPlaybackServiceWrapper serviceWrapper, BaseCursorAdapter adapter,
                                BaseLoaderCreator loaderCreator) {
        QueueLoaderCreator creator = (QueueLoaderCreator)loaderCreator;
        adapter.setIndicatorFor(creator.getTrackIdColumn(),
                serviceWrapper.getTrackID(), serviceWrapper.isPlaying());
    }

    private BroadcastReceiver mQueueChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            QueueLoaderCreator creator = (QueueLoaderCreator) mLoaderCreator;
            creator.setCurrentQueue(mServiceWrapper.getQueue());
            restartLoader();
        }
    };
}
