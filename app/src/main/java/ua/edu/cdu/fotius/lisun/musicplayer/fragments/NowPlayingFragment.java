package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.DragNDropListView;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.NowPlayingCursorLoaderCreator;

public class NowPlayingFragment extends BaseListFragment {

    public static String TAG = "now_playing_tag";

    private ToolbarStateListener mToolbarStateListener;

    public NowPlayingFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarStateListener = (ToolbarStateListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        long[] nowPlayingQueue = mServiceWrapper.getQueue();
        return new NowPlayingCursorLoaderCreator(getActivity(), nowPlayingQueue);
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
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, new TrackMenuCommandSet(this, mServiceWrapper),
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
}
