package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand.AddToPlaylistResultListener;

/**
 * Created by andrei on 22.10.2015.
 */
public abstract class BaseDialog {

    protected Context mContext;
    protected long[] mTrackIds;
    protected AddToPlaylistResultListener mListener;

    public BaseDialog(Context context, long[] trackIds, AddToPlaylistResultListener listener) {
        mContext = context;
        mTrackIds = trackIds;
        mListener = listener;
    }

    public abstract void show();
}
