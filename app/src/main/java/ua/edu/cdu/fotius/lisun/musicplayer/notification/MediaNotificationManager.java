package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.NotificationCompat;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class MediaNotificationManager {

    private MediaPlaybackService mService;

    private final int mNotificationID = 1;

    private PendingIntent mPlayAction;
    private PendingIntent mNextAction;
    private PendingIntent mPrevAction;

    private MediaControllerCompat.TransportControls mTransportControls;

    private MediaControlActionsReceiver mMediaControlActionsReceiver;

    public MediaNotificationManager(MediaPlaybackService service, MediaControllerCompat.TransportControls controls) {
        mService = service;
        mTransportControls = controls;

        mPlayAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PLAY), 0);

        mNextAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_NEXT), 0);

        mPrevAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PREV), 0);
    }

    private Notification createNotification(MediaMetadataCompat metaData) {
        Notification notification = new NotificationCompat.Builder(mService)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                .setContentText(metaData.getString(MediaMetadataCompat.METADATA_KEY_ALBUM) +
                        " - "+ metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setStyle(new NotificationCompat.MediaStyle())
                .addAction(R.mipmap.ic_launcher,
                        mService.getResources()
                                .getString(R.string.notification_action_previous), mPrevAction)
                .addAction(R.mipmap.ic_launcher,
                        mService.getResources()
                                .getString(R.string.notification_action_play_pause), mPlayAction)
                .addAction(R.mipmap.ic_launcher,
                        mService.getResources()
                                .getString(R.string.notification_action_next), mNextAction)
                .build();
        return notification;
    }

    public void startNotification(MediaMetadataCompat metadata) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaControlActionsReceiver.ACTION_PLAY);
        filter.addAction(MediaControlActionsReceiver.ACTION_NEXT);
        filter.addAction(MediaControlActionsReceiver.ACTION_PREV);
        mMediaControlActionsReceiver = new MediaControlActionsReceiver(mTransportControls);
        mService.registerReceiver(mMediaControlActionsReceiver, filter);

        Notification notification = createNotification(metadata);
        mService.startForeground(mNotificationID, notification);
        //TODO: start foreground
        //TODO: stopNotification
    }


    public void stopNotification() {
        mService.unregisterReceiver(mMediaControlActionsReceiver);
        mService.stopForeground(true);
    }
}
