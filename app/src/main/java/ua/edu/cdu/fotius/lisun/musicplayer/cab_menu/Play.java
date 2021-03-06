package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class Play extends Command {
    public Play(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mServiceWrapper.playAll(idsOverWhichToExecute, 0);
    }
}
