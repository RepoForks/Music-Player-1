package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;
import android.content.res.Resources;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AlbumMenuCommandSet extends BaseMenuCommandSet{

    private long mArtistID;

    public AlbumMenuCommandSet(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
        super(fragment, serviceWrapper);
        mArtistID = artistID;
    }

    @Override
    protected void initMinimalGroup(MenuCommandsContainer minimalGroup) {
        Resources resources = mFragment.getResources();
        minimalGroup.add(PLAY_ID, resources.getString(R.string.cab_menu_play), R.drawable.ic_play_white_24dp,
                new AlbumPlay(mFragment, mServiceWrapper, mArtistID),
                android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        minimalGroup.add(ADD_TO_PLAYLIST_ID, resources.getString(R.string.cab_menu_add_to_playlist),
                MenuItem.NO_ICON_ID, new AlbumAddToPlaylist(mFragment, mServiceWrapper, mArtistID),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(DELETE_ID, resources.getString(R.string.cab_menu_delete),
                MenuItem.NO_ICON_ID, new AlbumDelete(mFragment, mServiceWrapper, mArtistID),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
        minimalGroup.add(ADD_TO_PLAY_QUEUE_ID, resources.getString(R.string.cab_menu_add_to_queue),
                MenuItem.NO_ICON_ID, new AlbumAddToPlayQueue(mFragment, mServiceWrapper, mArtistID),
                android.view.MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void initAdditionalGroup(MenuCommandsContainer additionalGroup) {
    }
}
