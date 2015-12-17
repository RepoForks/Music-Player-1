package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.BaseMenuCommandSet;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AbstractTracksCursorLoaderCreator;

public abstract class BaseTracklistContentCreator {
    public abstract CursorAdapter createCursorAdapter(AbstractTracksCursorLoaderCreator loaderCreator,
                                                      Context context);
    public abstract ListView createListView(View v, LayoutInflater inflater, ViewGroup container);
    public abstract int getMultichoiceMode();
    public abstract BaseMenuCommandSet getMenuCommandSet(Context context,
                                                         MediaPlaybackServiceWrapper serviceWrapper);
}
