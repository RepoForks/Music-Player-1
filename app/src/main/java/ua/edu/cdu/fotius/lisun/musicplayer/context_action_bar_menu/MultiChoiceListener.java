package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.IndicatorCursorAdapter;


public class MultiChoiceListener implements AbsListView.MultiChoiceModeListener {
    private AbsListView mAbsListView;
    private BaseMenuCommandSet mBaseMenuCommandsSet;
    private Context mContext;
    private String mChoosingItemIdColumnName;

    public MultiChoiceListener(Context context,
                               AbsListView absListView, BaseMenuCommandSet baseMenuCommandSet, String choosingItemIdColumnName){
        mAbsListView = absListView;
        mBaseMenuCommandsSet = baseMenuCommandSet;
        mContext = context;
        mChoosingItemIdColumnName = choosingItemIdColumnName;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Resources resources = mContext.getResources();
        mode.setTitle(resources.getString(R.string.selected_text, mAbsListView.getCheckedItemCount()));
        initializeMenu(menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //mToolbarStateListener.showToolbar();
        mAbsListView.clearChoices();
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
        long[] checkedIds = getCheckedIds(checkedPositions);
        mBaseMenuCommandsSet.execute(menuItem.getItemId(), checkedIds);
        mode.finish();
        return true;
    }

    private ArrayList<Integer> getCheckedPositions() {
        ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
        for(int i = 0; i < mAbsListView.getCheckedItemPositions().size(); i++) {
            int position = mAbsListView.getCheckedItemPositions().keyAt(i);
            boolean value = mAbsListView.getCheckedItemPositions().get(position);
            if(value) {
                checkedPositions.add(position);
            }
        }
        return checkedPositions;
    }

    private long[] getCheckedIds(ArrayList<Integer> checkedPositions) {
        IndicatorCursorAdapter adapter = (IndicatorCursorAdapter) mAbsListView.getAdapter();
        Cursor cursor = adapter.getCursor();
        long[] checkedIds = new long[checkedPositions.size()];
        int idColumnIndex = cursor.getColumnIndexOrThrow(mChoosingItemIdColumnName);
        for(int i = 0; i < checkedPositions.size(); i++) {
            if(cursor.moveToPosition(checkedPositions.get(i))) {
                checkedIds[i] = cursor.getLong(idColumnIndex);
            }
        }
        return checkedIds;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean needToCheck) {
        boolean isAdditionalMenuNeeded = (mAbsListView.getCheckedItemCount() == 1) ? true : false;
        MenuCommandsContainer additionalCommands = mBaseMenuCommandsSet.getAdditionalGroup();
        if(additionalCommands != null) {
            mode.getMenu().setGroupVisible(additionalCommands.getId(), isAdditionalMenuNeeded);
        }
        Resources resources = mContext.getResources();
        mode.setTitle(resources.getString(R.string.selected_text, mAbsListView.getCheckedItemCount()));
    }
}