package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class TrackMenuCommandSet extends BaseMenuCommandSet {

    public static int DELETE_ID = 1;
    public static int PLAY_ID = 2;
    public static int AS_RINGTONE_ID = 3;
    public static int ADD_TO_PLAY_QUEUE_ID = 4;
    public static int ADD_TO_PLAYLIST_ID = 5;
    public static int EDIT_INFO_ID = 6;

    private Context mContext;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public TrackMenuCommandSet(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        mContext = context;
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            //TODO: strings to string.xml
            group.add(DELETE_ID, "Delete", R.drawable.ic_delete_black_24dp, new Delete(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            group.add(PLAY_ID, "Play", R.mipmap.ic_launcher, new Play(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, "Add To Play Queue", R.mipmap.ic_launcher, new AddToPlayQueue(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST_ID, "Add To Playlist", R.mipmap.ic_launcher, new AddToPlaylist(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setMinimalGroup(group); //need to avoid creating next time
        }
        return group;
    }

    //TODO: what? ic_launcher?

    @Override
    public MenuCommandsContainer initializeOrGetAdditionalGroup() {
        MenuCommandsContainer group = getAdditionalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.ADDITIONAL_GROUP_ID);
            //TODO: strings to string.xml
            group.add(AS_RINGTONE_ID, "As ringtone", R.mipmap.ic_launcher, new AsRingtone(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(EDIT_INFO_ID, "Edit Info", R.mipmap.ic_launcher, new EditTrackInfoCommand(mContext),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setAdditionalGroup(group); //need to avoid creating next time
        }
        return group;
    }
}
