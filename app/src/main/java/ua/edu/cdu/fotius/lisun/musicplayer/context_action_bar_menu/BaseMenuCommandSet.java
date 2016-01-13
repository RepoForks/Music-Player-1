package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public abstract class BaseMenuCommandSet {

    public static int DELETE_ID = 1;
    public static int PLAY_ID = 2;
    public static int AS_RINGTONE_ID = 3;
    public static int ADD_TO_PLAY_QUEUE_ID = 4;
    public static int ADD_TO_PLAYLIST_ID = 5;
    public static int EDIT_INFO_ID = 6;

    protected Fragment mFragment;
    protected PlaybackServiceWrapper mServiceWrapper;

    private MenuCommandsContainer mMinimalGroup = null;
    private MenuCommandsContainer mAdditionalGroup = null;

    protected BaseMenuCommandSet(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        mFragment = fragment;
        mServiceWrapper = serviceWrapper;
    }

    protected MenuCommandsContainer getMinimalGroup() {
        return mMinimalGroup;
    }

    protected void setMinimalGroup(MenuCommandsContainer minimalGroup) {
        mMinimalGroup = minimalGroup;
    }

    protected MenuCommandsContainer getAdditionalGroup() {
        return mAdditionalGroup;
    }

    protected void setAdditionalGroup(MenuCommandsContainer additionalGroup) {
        mAdditionalGroup = additionalGroup;
    }

    public void execute(int itemId, long[] idsOverWhichToExecute) {
        if(mMinimalGroup == null) {
            return;
        }

        boolean isExecuted = mMinimalGroup.execute(itemId, idsOverWhichToExecute);
        if(!isExecuted) {
            if(mAdditionalGroup != null) {
                mAdditionalGroup.execute(itemId, idsOverWhichToExecute);
            }
        }
    }

    public abstract MenuCommandsContainer initializeOrGetMinimalGroup();
    public abstract MenuCommandsContainer initializeOrGetAdditionalGroup();
}
