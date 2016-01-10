package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BaseDialog {

    protected Fragment mFragment;
    protected long[] mTrackIds;

    public BaseDialog(Fragment fragment, long[] trackIds) {
        mFragment = fragment;
        mTrackIds = trackIds;
    }

    public abstract void show();
}
