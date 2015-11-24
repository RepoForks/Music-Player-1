package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;


public class MultiChoiceListener implements AbsListView.MultiChoiceModeListener {

    private final String TAG = getClass().getSimpleName();
    private ToolbarStateListener mToolbarStateListener;
    private ListView mListView;
    private BaseMenuCommandSet mBaseMenuCommandsSet;
    private Context mContext;

    public MultiChoiceListener(Context context, ToolbarStateListener toolbarStateListener, ListView listView, BaseMenuCommandSet baseMenuCommandSet){
        mToolbarStateListener = toolbarStateListener;
        mListView = listView;
        mBaseMenuCommandsSet = baseMenuCommandSet;
        mContext = context;
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
        MenuWrapper menuWrapper = new MenuWrapper(menu);

        MenuCommandsContainer commandsContainer = mBaseMenuCommandsSet.initializeOrGetMinimalGroup();
        menuWrapper.setContent(commandsContainer);

        commandsContainer = mBaseMenuCommandsSet.initializeOrGetAdditionalGroup();
        menuWrapper.setContent(commandsContainer);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        ArrayList<Integer> checkedPositions = getCheckedPositions();
        long[] checkedTrackIds = getCheckedTrackIds(checkedPositions);
        mBaseMenuCommandsSet.execute(menuItem.getItemId(), checkedTrackIds);
        mode.finish();
        return true;
    }

    private ArrayList<Integer> getCheckedPositions() {
        ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
        for(int i = 0; i < mListView.getCheckedItemPositions().size(); i++) {
            int position = mListView.getCheckedItemPositions().keyAt(i);
            boolean value = mListView.getCheckedItemPositions().get(position);
            if(value) {
                checkedPositions.add(position);
            }
        }
        return checkedPositions;
    }

    private long[] getCheckedTrackIds(ArrayList<Integer> checkedPositions) {
        BaseSimpleCursorAdapter adapter = (BaseSimpleCursorAdapter)mListView.getAdapter();
        Cursor cursor = adapter.getCursor();
        long[] checkedTrackIds = new long[checkedPositions.size()];
        //TODO: AudioStorage.Track.TRACK_ID is too precise. Need to be more flippy.
        int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.Track.TRACK_ID);
        for(int i = 0; i < checkedPositions.size(); i++) {
            if(cursor.moveToPosition(checkedPositions.get(i))) {
                checkedTrackIds[i] = cursor.getLong(idColumnIndex);
            }
        }
        return checkedTrackIds;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean needToCheck) {
        boolean isAdditionalMenuNeeded = (mListView.getCheckedItemCount() == 1) ? true : false;
        mode.getMenu().setGroupVisible(mBaseMenuCommandsSet.getAdditionalGroup().getId(), isAdditionalMenuNeeded);
        Resources resources = mContext.getResources();
        mode.setTitle(mListView.getCheckedItemCount() + " " + resources.getString(R.string.selected_text));
    }
}