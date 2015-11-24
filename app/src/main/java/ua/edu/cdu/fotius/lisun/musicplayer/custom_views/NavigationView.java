package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;

public class NavigationView extends android.support.design.widget.NavigationView{
    public NavigationView(Context context) {
        super(context);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedCheckedState savedCheckedState = new SavedCheckedState(parcelable);
        savedCheckedState.checkedMenuItemId = getCheckedItemId();
        return savedCheckedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable savedState) {
        SavedCheckedState ss = (SavedCheckedState) savedState;
        super.onRestoreInstanceState(ss.getSuperState());
        int itemToCheck = ss.checkedMenuItemId;
        getMenu().findItem(itemToCheck).setChecked(true);
    }

    private int getCheckedItemId() {
        Menu menu = getMenu();
        for(int i = 0; i < menu.size(); i++) {
            if(menu.getItem(i).isChecked()) {
                return menu.getItem(i).getItemId();
            }
        }
        return -1;
    }
}
