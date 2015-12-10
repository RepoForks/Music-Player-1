package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;


public class Delete extends Command {

    public Delete(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new DeleteDialog(mContext, idsOverWhichToExecute, mServiceWrapper).show();
    }
}
