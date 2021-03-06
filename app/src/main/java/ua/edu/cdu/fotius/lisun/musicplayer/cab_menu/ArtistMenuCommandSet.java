package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ArtistMenuCommandSet extends BaseMenuCommandSet{

    public ArtistMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Resources resources = mFragment.getResources();
        minimalGroup.add(PLAY_ID, resources.getString(R.string.cab_menu_play), R.drawable.ic_play_white_24dp,
                new ArtistPlay(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        minimalGroup.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                MenuItem.NO_ICON_ID, new ArtistAddToPlaylist(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                MenuItem.NO_ICON_ID, new ArtistDelete(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                MenuItem.NO_ICON_ID, new ArtistAddToPlayQueue(mFragment, mServiceWrapper),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void initAdditionalGroup(MenuCommandsContainer additionalGroup) {
    }
}
