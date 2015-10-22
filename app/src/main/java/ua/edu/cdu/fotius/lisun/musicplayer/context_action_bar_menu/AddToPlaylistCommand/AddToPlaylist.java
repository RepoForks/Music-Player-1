package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.Command;

public class AddToPlaylist extends Command {

    public AddToPlaylist(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new ChoosePlaylistDialog(mContext, idsOverWhichToExecute).show();
    }
}
