package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;


public class MultiChoiceListener implements AbsListView.MultiChoiceModeListener {

    private final String TAG = getClass().getSimpleName();
    private ToolbarStateListener mToolbarStateListener;
    private ListView mListView;
    private BaseMenu mMenu;

    public MultiChoiceListener(ToolbarStateListener toolbarStateListener, ListView listView, BaseMenu baseMenu){
        mToolbarStateListener = toolbarStateListener;
        mListView = listView;
        mMenu = baseMenu;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mToolbarStateListener.showToolbar();
        mListView.clearChoices();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        initializeMenu(menu);
        mToolbarStateListener.hideToolbar();
        return true;
    }

    private void initializeMenu(Menu menu) {
        MenuGroup group = mMenu.initializeOrGetMinimalGroup();
        group.fillMenuWithThisGroup(menu);
        group = mMenu.initializeOrGetAdditionalGroup();
        group.fillMenuWithThisGroup(menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        //TODO: Refactor
        ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
        for(int i = 0; i < mListView.getCheckedItemPositions().size(); i++) {
            int position = mListView.getCheckedItemPositions().keyAt(i);
            boolean value = mListView.getCheckedItemPositions().get(position);
            if(value) {
                checkedPositions.add(position);
            }
        }

        BaseSimpleCursorAdapter adapter = (BaseSimpleCursorAdapter)mListView.getAdapter();
        Cursor cursor = adapter.getCursor();
        ArrayList<Long> checkedTrackIds = new ArrayList<Long>();
        //TODO: AudioStorage.Track.TRACK_ID is too precise. Need to be more flippy.
        int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.Track.TRACK_ID);
        for(Integer checkedPosition : checkedPositions) {
            if(cursor.moveToPosition(checkedPosition)) {
                checkedTrackIds.add(cursor.getLong(idColumnIndex));
            }
        }

        mMenu.execute(menuItem.getItemId(), checkedTrackIds);
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean needToCheck) {
        boolean isAdditionalMenuNeeded = (mListView.getCheckedItemCount() == 1) ? true : false;
        mode.getMenu().setGroupVisible(mMenu.getAdditionalGroup().getId(), isAdditionalMenuNeeded);
        //TODO: to string.xml
        mode.setTitle(mListView.getCheckedItemCount() + " selected");
    }
}