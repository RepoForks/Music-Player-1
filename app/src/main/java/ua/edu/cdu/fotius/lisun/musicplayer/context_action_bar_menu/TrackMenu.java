package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class TrackMenu extends BaseMenu {

    public static int DELETE_ID = 1;
    public static int PLAY_ID = 2;
    public static int AS_RINGTONE_ID = 3;
    public static int ADD_TO_PLAY_QUEUE_ID = 4;
    public static int ADD_TO_PLAYLIST = 5;

    private Context mContext;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public TrackMenu(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        mContext = context;
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public MenuGroup initializeOrGetMinimalGroup() {
        MenuGroup group = getMinimalGroup();
        if(group == null) {
            group = new MenuGroup(MenuGroup.MINIMAL_GROUP_ID);
            //TODO: strings to string.xml
            group.add(DELETE_ID, "Delete", R.mipmap.ic_delete_black_24dp, new Delete(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            group.add(PLAY_ID, "Play", R.mipmap.ic_launcher, new Play(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, "Add To Play Queue", R.mipmap.ic_launcher, new AddToPlayQueue(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST, "Add To Playlist", R.mipmap.ic_launcher, new AddToPlaylist(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setMinimalGroup(group); //need to avoid creating next time
        }
        return group;
    }

    @Override
    public MenuGroup initializeOrGetAdditionalGroup() {
        MenuGroup group = getAdditionalGroup();
        if(group == null) {
            group = new MenuGroup(MenuGroup.ADDITIONAL_GROUP_ID);
            //TODO: strings to string.xml
            group.add(AS_RINGTONE_ID, "As ringtone", R.mipmap.ic_launcher, new AsRingtone(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setAdditionalGroup(group); //need to avoid creating next time
        }
        return group;
    }
}
