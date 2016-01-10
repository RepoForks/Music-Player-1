package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AddToPlaylist extends Command {

    public AddToPlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] tracksID) {
        new ChoosePlaylistDialog(mFragment, tracksID).show();
    }
}
