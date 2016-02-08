package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.res.Resources;
import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class PlaylistMenuCommandSet extends BaseMenuCommandSet {

    public PlaylistMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        minimalGroup.add(DELETE_PLAYLIST, resources.getString(R.string.cab_menu_delete_playlist),
                MenuItem.NO_ICON_ID, new DeletePlaylist(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void initAdditionalGroup(MenuCommandsContainer additionalGroup) {
        Context context = mFragment.getActivity();
        Resources resources = context.getResources();
        additionalGroup.add(RENAME_PLAYLIST, resources.getString(R.string.cab_menu_rename_playlist),
                MenuItem.NO_ICON_ID, new RenamePlaylist(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
