package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.util.Log;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class Play extends Command{

    private final String TAG = getClass().getSimpleName();

    public Play(MediaPlaybackServiceWrapper serviceWrapper) {
        super(serviceWrapper);
    }

    @Override
    public void execute(ArrayList<Long> idsOverWhichToExecute) {
        Log.d(TAG, "Play Command");
    }
}
