package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * TODO: Maybe delete this
 */
public class PlaybackCommandsReceiver extends BroadcastReceiver{

    private MediaPlaybackService mService = null;

    public PlaybackCommandsReceiver(MediaPlaybackService service) {
        mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(mService != null) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");

            if (PlaybackCommands.CMDNEXT.equals(cmd) || PlaybackCommands.NEXT_ACTION.equals(action)) {
                //set next song and play it
                mService.gotoNext(true);
            } else if (PlaybackCommands.CMDPREVIOUS.equals(cmd)
                    || PlaybackCommands.PREVIOUS_ACTION.equals(action)) {
                //set mPlayPos to previous and play
                mService.prev();
            } else if (PlaybackCommands.CMDTOGGLEPAUSE.equals(cmd)
                    || PlaybackCommands.TOGGLEPAUSE_ACTION.equals(action)) {
                //TODO:
                if (mService.isPlaying()) {
                    mService.pause();
                    mService.setPausedByTransientLossOfFocus(false);
                } else {
                    mService.play();
                }
            } else if (PlaybackCommands.CMDPAUSE.equals(cmd)
                    || PlaybackCommands.PAUSE_ACTION.equals(action)) {
                mService.pause();
                mService.setPausedByTransientLossOfFocus(false);
            } else if (PlaybackCommands.CMDPLAY.equals(cmd)) {
                mService.play();
            } else if (PlaybackCommands.CMDSTOP.equals(cmd)) {
                mService.pause();
                mService.setPausedByTransientLossOfFocus(false);
                mService.seek(0);
            }
//            else if (MediaAppWidgetProvider.CMDAPPWIDGETUPDATE.equals(cmd)) {
//                // Someone asked us to refresh a set of specific widgets, probably
//                // because they were just added.
//                int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//            }
        }
    }
}
