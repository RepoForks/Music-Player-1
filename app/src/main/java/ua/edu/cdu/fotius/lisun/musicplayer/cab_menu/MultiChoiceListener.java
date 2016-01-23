package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.BaseCursorAdapter;


public class MultiChoiceListener implements AbsListView.MultiChoiceModeListener {

    private final String TAG = getClass().getSimpleName();

    private AbsListView mAbsListView;
    private BaseMenuCommandSet mBaseMenuCommandsSet;
    private Context mContext;
    private String mChoosingItemIdColumn;

    public MultiChoiceListener(Context context,
                               AbsListView absListView, BaseMenuCommandSet baseMenuCommandSet, String choosingItemIdColumn){
        mAbsListView = absListView;
        mBaseMenuCommandsSet = baseMenuCommandSet;
        mContext = context;
        mChoosingItemIdColumn = choosingItemIdColumn;
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
        BaseCursorAdapter adapter = (BaseCursorAdapter) mAbsListView.getAdapter();
        Cursor cursor = adapter.getCursor();
        long[] checkedIds = new long[checkedPositions.size()];
        int idColumnIndex = cursor.getColumnIndexOrThrow(mChoosingItemIdColumn);
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

        Log.d(TAG, "onItemCheckedStateChanged");
        View clickedView = mAbsListView.getChildAt(position);
        View checkedIndicator = clickedView.findViewById(R.id.checked_indicator);

        //TODO: here checked indicator sometimes null. Why?

        if(needToCheck) {
            checkedIndicator.setVisibility(View.VISIBLE);
        } else {
            checkedIndicator.setVisibility(View.GONE);
        }
    }
}