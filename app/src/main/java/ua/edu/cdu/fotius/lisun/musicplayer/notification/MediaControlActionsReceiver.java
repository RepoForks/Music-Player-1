package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaControllerCompat;

public class MediaControlActionsReceiver extends BroadcastReceiver{

    private final String TAG = getClass().getSimpleName();

    public static final String ACTION_PLAY = "ua.edu.cdu.fotius.lisun.action_play";
    public static final String ACTION_PAUSE = "ua.edu.cdu.fotius.lisun.action_pause";
    public static final String ACTION_PREV = "ua.edu.cdu.fotius.lisun.action_prev";
    public static final String ACTION_NEXT = "ua.edu.cdu.fotius.lisun.action_next";

    private MediaControllerCompat.TransportControls mTransportControls;

    public MediaControlActionsReceiver(MediaControllerCompat.TransportControls controls) {
        mTransportControls = controls;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case ACTION_PLAY :
                mTransportControls.play();
                break;
            case ACTION_PAUSE :
                mTransportControls.pause();
                break;
            case ACTION_PREV :
                mTransportControls.skipToPrevious();
                break;
            case ACTION_NEXT :
                mTransportControls.skipToNext();
                break;
        }
    }
}
