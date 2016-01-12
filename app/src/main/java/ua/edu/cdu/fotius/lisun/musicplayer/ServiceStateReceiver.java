package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStateReceiver extends BroadcastReceiver{

    private ServiceStateChangesObserver mStateObserver;

    public ServiceStateReceiver(ServiceStateChangesObserver stateObserver) {
        mStateObserver = stateObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(MediaPlaybackService.META_CHANGED)) {
            mStateObserver.onMetadataChanged();
        } else if (action.equals(MediaPlaybackService.PLAYSTATE_CHANGED)) {
            mStateObserver.onPlaybackStateChanged();
        }
    }
}
