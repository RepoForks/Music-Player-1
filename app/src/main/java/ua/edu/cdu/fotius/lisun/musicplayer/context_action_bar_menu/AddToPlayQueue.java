package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AddToPlayQueue extends Command{

    public AddToPlayQueue(MediaPlaybackServiceWrapper serviceWrapper) {
        super(serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mServiceWrapper.addToTheEndOfPlayingQueue(idsOverWhichToExecute);
    }
}
