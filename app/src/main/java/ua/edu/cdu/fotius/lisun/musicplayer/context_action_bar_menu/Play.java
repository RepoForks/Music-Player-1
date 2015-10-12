package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.util.Log;

public class Play extends Command{

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute() {
        Log.d(TAG, "Play Command");
    }
}
