package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AlbumMenuCommandSet extends BaseMenuCommandSet{

    private long mArtistID;

    public AlbumMenuCommandSet(Fragment fragment, PlaybackServiceWrapper serviceWrapper, long artistID) {
        super(fragment, serviceWrapper);
        mArtistID = artistID;
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            Resources resources = mFragment.getResources();
            group.add(PLAY_ID, resources.getString(R.string.cab_menu_play), R.mipmap.ic_launcher,
                    new AlbumPlay(mFragment, mServiceWrapper, mArtistID),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                    R.mipmap.ic_launcher, new AlbumAddToPlaylist(mFragment, mServiceWrapper, mArtistID),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                    R.mipmap.ic_launcher, new AlbumDelete(mFragment, mServiceWrapper, mArtistID),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            group.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                    R.mipmap.ic_launcher, new AlbumAddToPlayQueue(mFragment, mServiceWrapper, mArtistID),
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
