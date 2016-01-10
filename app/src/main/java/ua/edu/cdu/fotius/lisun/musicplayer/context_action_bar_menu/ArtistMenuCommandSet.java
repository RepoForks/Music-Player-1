package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ArtistMenuCommandSet extends BaseMenuCommandSet{

    public ArtistMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            Resources resources = mFragment.getResources();
            group.add(PLAY_ID, resources.getString(R.string.cab_menu_play), R.mipmap.ic_launcher,
                    new ArtistPlay(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                    R.mipmap.ic_launcher, new ArtistAddToPlaylist(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                    R.mipmap.ic_launcher, new ArtistDelete(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                    R.mipmap.ic_launcher, new ArtistAddToPlayQueue(mFragment, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setMinimalGroup(group); //need to avoid creating next time
        }
        return group;
    }

    @Override
    public MenuCommandsContainer initializeOrGetAdditionalGroup() {
        return null;
    }
}
