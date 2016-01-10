package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class TrackMenuCommandSet extends BaseMenuCommandSet {

    public TrackMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if (group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            Context context = mFragment.getActivity();
            Resources resources = context.getResources();
            group.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                    R.drawable.ic_delete_black_24dp, new Delete(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            group.add(PLAY_ID, resources.getString(R.string.cab_menu_play),
                    R.mipmap.ic_launcher, new Play(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                    R.mipmap.ic_launcher, new AddToPlayQueue(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                    R.mipmap.ic_launcher, new AddToPlaylist(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setMinimalGroup(group); //need to avoid creating next time
        }
        return group;
    }

    //TODO: what? ic_launcher?

    @Override
    public MenuCommandsContainer initializeOrGetAdditionalGroup() {
        MenuCommandsContainer group = getAdditionalGroup();
        if (group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.ADDITIONAL_GROUP_ID);
            Context context = mFragment.getActivity();
            Resources resources = context.getResources();
            group.add(AS_RINGTONE_ID, resources.getString(R.string.cab_menu_as_ringtone),
                    R.mipmap.ic_launcher, new AsRingtone(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(EDIT_INFO_ID, resources.getString(R.string.cab_menu_edit_into),
                    R.mipmap.ic_launcher, new EditTrackInfoCommand(mFragment),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setAdditionalGroup(group); //need to avoid creating next time
        }
        return group;
    }
}
