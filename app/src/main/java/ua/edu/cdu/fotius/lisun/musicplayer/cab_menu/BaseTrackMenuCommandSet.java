package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.content.Context;
import android.content.res.Resources;
import android.app.Fragment;
import android.view.*;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public abstract class BaseTrackMenuCommandSet extends BaseMenuCommandSet {

    public BaseTrackMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                MenuItem.NO_ICON_ID, new Delete(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(PLAY_ID, resources.getString(R.string.cab_menu_play),
                R.drawable.ic_play_white_24dp, new Play(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        minimalGroup.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                MenuItem.NO_ICON_ID, new AddToPlaylist(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void initAdditionalGroup(MenuCommandsContainer additionalGroup) {
        additionalGroup = new MenuCommandsContainer(MenuCommandsContainer.ADDITIONAL_GROUP_ID);
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        additionalGroup.add(AS_RINGTONE_ID, resources.getString(R.string.cab_menu_as_ringtone),
                MenuItem.NO_ICON_ID, new AsRingtone(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        additionalGroup.add(EDIT_INFO_ID, resources.getString(R.string.cab_menu_edit_into),
                MenuItem.NO_ICON_ID, new EditTrackInfoCommand(mFragment),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        setAdditionalGroup(additionalGroup); //need to avoid creating next time
    }
}
