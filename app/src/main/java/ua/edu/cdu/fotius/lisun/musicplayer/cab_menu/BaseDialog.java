package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.app.Fragment;

public abstract class BaseDialog {
    protected Fragment mFragment;
    protected long[] mIds;

    public BaseDialog(Fragment fragment, long[] ids) {
        mFragment = fragment;
        mIds = ids;
    }

    public abstract void show();
}
