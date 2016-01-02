package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaControllerCompat;

public class MediaControlActionsReceiver extends BroadcastReceiver{

    private final String TAG = getClass().getSimpleName();

    public static String ACTION_PLAY = "ua.edu.cdu.fotius.lisun.action_play";
    public static String ACTION_PREV = "ua.edu.cdu.fotius.lisun.action_prev";
    public static String ACTION_NEXT = "ua.edu.cdu.fotius.lisun.action_next";

    private MediaControllerCompat.TransportControls mTransportControls;

    public MediaControlActionsReceiver(MediaControllerCompat.TransportControls controls) {
        mTransportControls = controls;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_PLAY)) {
            mTransportControls.play();
        } else if(action.equals(ACTION_NEXT)) {
            mTransportControls.skipToNext();
        } else if(action.equals(ACTION_PREV)) {
            mTransportControls.skipToPrevious();
        }
    }
}
