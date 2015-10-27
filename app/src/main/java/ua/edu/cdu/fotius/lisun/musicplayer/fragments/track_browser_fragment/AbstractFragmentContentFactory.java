package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenu;

public abstract class AbstractFragmentContentFactory {

    protected Context mContext;
    protected MediaPlaybackServiceWrapper mServiceWrapper;
    protected CursorAdapter mCursorAdapter;

    public AbstractFragmentContentFactory(Context context, MediaPlaybackServiceWrapper serviceWrapper, CursorAdapter cursorAdapter) {
        mContext = context;
        mServiceWrapper = serviceWrapper;
        mCursorAdapter = cursorAdapter;
    }

    public abstract int getLayoutID();
    public abstract int getListViewResourceID();
    public abstract BaseMenu getActionBarMenuContent();
    public abstract int getChoiceMode();
    public abstract AdapterView.OnItemClickListener getOnItemClickListener();
    public abstract DynamicListView.OnDragNDropEventListener getOnDragNDropEventListener();
}
