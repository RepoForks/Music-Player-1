package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenu;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.TrackMenu;

public class TracksFragmentContenFactory extends AbstractFragmentContentFactory{

    private String mTrackIdColumnName;

    public TracksFragmentContenFactory(Context context, MediaPlaybackServiceWrapper serviceWrapper,
                                       CursorAdapter cursorAdapter, String trackIdColumnName) {
        super(context, serviceWrapper, cursorAdapter);
        mTrackIdColumnName = trackIdColumnName;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_tracks_browser;
    }

    @Override
    public int getListViewResourceID() {
        return R.id.list;
    }

    @Override
    public BaseMenu getActionBarMenuContent() {
        return new TrackMenu(mContext, mServiceWrapper);
    }

    @Override
    public int getChoiceMode() {
        return ListView.CHOICE_MODE_MULTIPLE_MODAL;
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new OnTrackClick(mContext, mCursorAdapter, mServiceWrapper, mTrackIdColumnName);
    }

    @Override
    public DynamicListView.OnDragNDropEventListener getOnDragNDropEventListener() {
        return null;
    }
}
