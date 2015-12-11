package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.res.Resources;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ArtistMenuCommandSet extends BaseMenuCommandSet{

    public ArtistMenuCommandSet(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public MenuCommandsContainer initializeOrGetMinimalGroup() {
        MenuCommandsContainer group = getMinimalGroup();
        if(group == null) {
            group = new MenuCommandsContainer(MenuCommandsContainer.MINIMAL_GROUP_ID);
            Resources resources = mContext.getResources();
            group.add(PLAY_ID, resources.getString(R.string.cab_menu_play), R.mipmap.ic_launcher, new PlayArtist(mContext, mServiceWrapper),
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
