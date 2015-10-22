package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.Command;

public class AddToPlaylist extends Command implements AddToPlaylistResultListener{

    public AddToPlaylist(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new ChoosePlaylistDialog(mContext, idsOverWhichToExecute, this).show();
    }

    @Override
    public void addedToPlaylistUserNotification(int addedQuantity, String playlistName) {
        String ending = ((addedQuantity == 1) ? "" : "s");
        String message = mContext.getResources().getString(R.string.tracks_added_to_playlist, addedQuantity, ending, playlistName);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
