package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

public abstract class BaseDialog {

    protected Context mContext;
    protected long[] mTrackIds;

    public BaseDialog(Context context, long[] trackIds) {
        mContext = context;
        mTrackIds = trackIds;
    }

    public abstract void show();
}
