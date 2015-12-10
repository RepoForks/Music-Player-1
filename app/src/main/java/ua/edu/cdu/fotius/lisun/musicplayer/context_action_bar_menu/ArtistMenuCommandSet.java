package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ArtistMenuCommandSet extends BaseMenuCommandSet{

    private Context mContext;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    //TODO: move to super class with AlbumMenuCommandsSet
    public ArtistMenuCommandSet(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        mContext = context;
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            //TODO: strings to string.xml
            group.add(PLAY_ID, "Play", R.mipmap.ic_launcher, new PlayArtist(mContext, mServiceWrapper),
                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
//
//            group.add(ADD_TO_PLAYLIST_ID, "Add To Playlist", R.mipmap.ic_launcher, new AddToPlaylist(mContext, mServiceWrapper),
//                    android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            setMinimalGroup(group); //need to avoid creating next time
        }
        return group;
    }

    @Override
    public MenuCommandsContainer initializeOrGetAdditionalGroup() {
        return null;
    }
}
