package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;

public class MediaNotificationManager {

    private final String TAG = getClass().getSimpleName();

    private MediaPlaybackService mService;

    private final int mNotificationID = 1;

    private PendingIntent mPlayAction;
    private PendingIntent mPauseAction;
    private PendingIntent mNextAction;
    private PendingIntent mPrevAction;

    private MediaControlActionsReceiver mMediaControlActionsReceiver;
    private NotificationManager mNotificationManager;

    private boolean mIsStarted = false;

    public MediaNotificationManager(MediaPlaybackService service, MediaControllerCompat.TransportControls controls) {
        mService = service;

        Log.d(TAG, "MediaNotificationManager constructor");
        mMediaControlActionsReceiver =
                new MediaControlActionsReceiver(controls);

        mPlayAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PLAY), 0);
        mPauseAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PAUSE), 0);
        mNextAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_NEXT), 0);
        mPrevAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PREV), 0);

        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancelAll();
    }

    public void startOrUpdateNotification(MediaMetadataCompat metadata, long playingAlbumId, boolean isPlaying) {
        Notification notification = createNotification(metadata, playingAlbumId, isPlaying);
        if(!mIsStarted) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(MediaControlActionsReceiver.ACTION_PLAY);
            filter.addAction(MediaControlActionsReceiver.ACTION_PAUSE);
            filter.addAction(MediaControlActionsReceiver.ACTION_NEXT);
            filter.addAction(MediaControlActionsReceiver.ACTION_PREV);
            mService.registerReceiver(mMediaControlActionsReceiver, filter);
            Log.d(TAG, "before start foreground-----");
            mService.startForeground(mNotificationID, notification);
            Log.d(TAG, "after start foreground-----");
        } else {
            mNotificationManager.notify(mNotificationID, notification);
        }
    }


    private Notification createNotification(MediaMetadataCompat metaData, long playingAlbumId, boolean isPlaying) {
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(mService)
                .setContentTitle(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                .setContentText(metaData.getString(MediaMetadataCompat.METADATA_KEY_ALBUM) +
                        " - "+ metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setStyle(new NotificationCompat.MediaStyle())
                .addAction(R.mipmap.ic_launcher,
                        mService.getResources()
                                .getString(R.string.notification_action_previous), mPrevAction)
                .addAction(getPlayPauseAction(isPlaying))
                .addAction(R.mipmap.ic_launcher,
                        mService.getResources()
                                .getString(R.string.notification_action_next), mNextAction);

        FakeImageView fakeImageView = new FakeImageView(mService, mNotificationManager, builder, mNotificationID);
        fakeImageView.setMaxWidth(50);
        fakeImageView.setMaxHeight(50);
        ImageLoader loader = new ImageLoader(mService);
        loader.load(playingAlbumId).withDefault(R.drawable.default_album_art_512dp).into(fakeImageView);

        return builder.build();
    }

    private android.support.v4.app.NotificationCompat.Action getPlayPauseAction(boolean isPlaying) {
        int icon;
        String title;
        PendingIntent intent;
        if(isPlaying) {
            icon = R.drawable.ic_pause_circle_filled_black_32dp;
            title = mService.getResources().getString(R.string.notification_action_pause);
            intent = mPauseAction;
        } else {
            icon = R.drawable.ic_play_circle_filled_black_32dp;
            title = mService.getResources().getString(R.string.notification_action_play);
            intent = mPlayAction;
        }
        android.support.v4.app.NotificationCompat.Action.Builder actionBuilder
                = new android.support.v4.app.NotificationCompat.Action.Builder(icon, title, intent);
        return actionBuilder.build();
    }

    public void stopNotification() {
        Log.d(TAG, "MediaNotificationManager stopNotification");
        mService.unregisterReceiver(mMediaControlActionsReceiver);
        mService.stopForeground(true);
        mNotificationManager.cancel(mNotificationID);
        mIsStarted = false;
    }
}
