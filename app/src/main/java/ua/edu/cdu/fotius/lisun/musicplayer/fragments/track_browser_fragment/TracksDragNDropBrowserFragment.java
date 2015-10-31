package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.MultiChoiceListener;

public class TracksDragNDropBrowserFragment extends TrackBrowserFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView ---> TracksDragNDropBrowserFragment");
        View v = inflater.inflate(getLayoutID(), container, false);
        DragNDropListView listView = (DragNDropListView) v.findViewById(getListViewResourceID());
        listView.setDragNDropAdapter((DragNDropCursorAdapter)mCursorAdapter);
        listView.setDragNDropEventListener((DragNDropCursorAdapter)mCursorAdapter);
        listView.setOnItemClickListener(createOnItemClickListener());
        listView.setChoiceMode(getChoiceMode());
        listView.setMultiChoiceModeListener(new MultiChoiceListener(getActivity(),
                mToolbarStateListener, listView, createActionBarMenuContent()));
        return v;
    }
}
