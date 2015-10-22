package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class Play extends Command{

    private final String TAG = getClass().getSimpleName();

    public Play(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        for(int i = 0; i < idsOverWhichToExecute.length; i++) {
            Log.d(TAG, "id(" + i + ") --> " + idsOverWhichToExecute[i]);
        }
        mServiceWrapper.playAll(idsOverWhichToExecute, 0);
    }
}
