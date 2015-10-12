package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


public class MultiChoiceListener implements AbsListView.MultiChoiceModeListener {

    private final String TAG = getClass().getSimpleName();
    private Toolbar mToolbar;
    private ListView mListView;
    private BaseMenu mMenu;

    public MultiChoiceListener(Toolbar toolbar, ListView listView, BaseMenu baseMenu){
        mToolbar = toolbar;
        mListView = listView;
        mMenu = baseMenu;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mToolbar.setVisibility(View.VISIBLE);
        mListView.clearChoices();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        initializeMenu(menu);
        //mToolbar.setVisibility(View.GONE);
        return true;
    }

    private void initializeMenu(Menu menu) {
        MenuGroup group = mMenu.initializeOrGetMinimalGroup();
        group.fillMenuWithThisGroup(menu);
        group = mMenu.initializeOrGetAdditionalGroup();
        group.fillMenuWithThisGroup(menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        mMenu.execute(item.getItemId());
        Log.d(TAG, "onActionItemClicked: " + mListView.getCheckedItemPositions().size());
        for(int i = 0; i < mListView.getCheckedItemPositions().size(); i++) {
            int key = mListView.getCheckedItemPositions().keyAt(i);
            boolean value = mListView.getCheckedItemPositions().get(key);
            Log.d(TAG, "position: " + key + " value: " + value);
        }
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean needToCheck) {
        boolean isAdditionalMenuNeeded = (mListView.getCheckedItemCount() == 1) ? true : false;
        mode.getMenu().setGroupVisible(mMenu.getAdditionalGroup().getId(), isAdditionalMenuNeeded);
        mode.setTitle(mListView.getCheckedItemCount() + " selected");
    }
}