package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.res.Resources;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class TrackMenuCommandSet extends BaseMenuCommandSet {

    //TODO: maybe move to superclass with other classes
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
            Resources resources = mContext.getResources();
            group.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                    R.drawable.ic_delete_black_24dp, new Delete(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            group.add(PLAY_ID, resources.getString(R.string.cab_menu_play),
                    R.mipmap.ic_launcher, new Play(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                    R.mipmap.ic_launcher, new AddToPlayQueue(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                    R.mipmap.ic_launcher, new AddToPlaylist(mContext, mServiceWrapper),
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
            Resources resources = mContext.getResources();
            group.add(AS_RINGTONE_ID, resources.getString(R.string.cab_menu_as_ringtone),
                    R.mipmap.ic_launcher, new AsRingtone(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(EDIT_INFO_ID, resources.getString(R.string.cab_menu_edit_into),
                    R.mipmap.ic_launcher, new EditTrackInfoCommand(mContext),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setAdditionalGroup(group); //need to avoid creating next time
        }
        return group;
    }
}
