package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public abstract class BaseTrackMenuCommandSet extends BaseMenuCommandSet {

    public BaseTrackMenuCommandSet(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                R.drawable.ic_delete_black_24dp, new Delete(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        minimalGroup.add(PLAY_ID, resources.getString(R.string.cab_menu_play),
                R.mipmap.ic_launcher, new Play(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                R.mipmap.ic_launcher, new AddToPlaylist(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void initAdditionalGroup(MenuCommandsContainer additionalGroup) {
        additionalGroup = new MenuCommandsContainer(MenuCommandsContainer.ADDITIONAL_GROUP_ID);
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        additionalGroup.add(AS_RINGTONE_ID, resources.getString(R.string.cab_menu_as_ringtone),
                R.mipmap.ic_launcher, new AsRingtone(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        additionalGroup.add(EDIT_INFO_ID, resources.getString(R.string.cab_menu_edit_into),
                R.mipmap.ic_launcher, new EditTrackInfoCommand(mFragment),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        setAdditionalGroup(additionalGroup); //need to avoid creating next time
    }
}
