package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AddToPlaylist extends Command {
    public AddToPlaylist(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] tracksID) {
        new ChoosePlaylistDialog(mContext, tracksID).show();
    }
}
