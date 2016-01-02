package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;

public class MediaControllerCallback extends MediaControllerCompat.Callback {

    private MediaPlaybackService mService;
    private MediaControllerCompat.TransportControls mTransportControls;

    public MediaControllerCallback(MediaPlaybackService service,
                                   MediaControllerCompat.TransportControls transportControls) {
        mService = service;
        mTransportControls = transportControls;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        super.onMetadataChanged(metadata);
        updateNotification();
    }

    private void updateNotification() {
        MediaNotificationManager mediaNotificationManager = new MediaNotificationManager(mService,
                mTransportControls);
    }
}
