package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.util.Log;

public class Delete extends Command{

    private final String TAG = getClass().getSimpleName();

    @Override
    public void execute() {
        Log.d(TAG, "Delete command");
    }
}
