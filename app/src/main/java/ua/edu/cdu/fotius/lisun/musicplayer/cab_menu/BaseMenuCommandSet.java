package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public abstract class BaseMenuCommandSet {
    public static int DELETE_ID = 1;
    public static int PLAY_ID = 2;
    public static int AS_RINGTONE_ID = 3;
    public static int ADD_TO_PLAY_QUEUE_ID = 4;
    public static int ADD_TO_PLAYLIST_ID = 5;
    public static int EDIT_INFO_ID = 6;
    public static int REMOVE_FROM_PLAY_QUEUE_ID = 7;
    public static int REMOVE_FROM_PLAYLIST = 8;
    public static int DELETE_PLAYLIST = 9;
    public static int RENAME_PLAYLIST = 10;

    protected Fragment mFragment;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    private MenuCommandsContainer mMinimalGroup = null;
    private MenuCommandsContainer mAdditionalGroup = null;

    protected BaseMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
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

    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        if(mMinimalGroup == null) {
            mMinimalGroup = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            initMinimalGroup(mMinimalGroup);
        }
        return mMinimalGroup;
    }

    public MenuCommandsContainer initializeOrGetAdditionalGroup() {
        if(mAdditionalGroup == null) {
            mAdditionalGroup = new MenuCommandsContainer(MenuCommandsContainer.ADDITIONAL_GROUP_ID);
            initAdditionalGroup(mAdditionalGroup);
        }
        return mAdditionalGroup;
    }

    protected abstract void initMinimalGroup(MenuCommandsContainer minimalGroup);
    protected abstract void initAdditionalGroup(MenuCommandsContainer additionalGroup);
}
