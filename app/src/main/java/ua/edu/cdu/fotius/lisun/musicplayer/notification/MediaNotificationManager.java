package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.NavigationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.SlidingPanelActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageLoader;

public class MediaNotificationManager {

    private MediaPlaybackService mService;

    private final int mNotificationID = 1;

    private PendingIntent mPlayAction;
    private PendingIntent mPauseAction;
    private PendingIntent mNextAction;
    private PendingIntent mPrevAction;
    private PendingIntent mContentAction;

    private MediaControlActionsReceiver mMediaControlActionsReceiver;
    private NotificationManager mNotificationManager;

    private ImageLoader mImageLoader;

    private Resources mResources;


    private boolean mIsStarted = false;

    public MediaNotificationManager(MediaPlaybackService service, MediaControllerCompat.TransportControls controls) {
        mService = service;
        mResources = mService.getResources();
        mImageLoader = new ImageLoader(mService);

        mMediaControlActionsReceiver =
                new MediaControlActionsReceiver(controls);

        mPlayAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PLAY), 0);
        mPauseAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PAUSE), 0);
        mNextAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_NEXT), 0);
        mPrevAction = PendingIntent.getBroadcast(mService, 0, new Intent(MediaControlActionsReceiver.ACTION_PREV), 0);
        mContentAction = createContentAction();

        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancelAll();
    }

    public PendingIntent createContentAction() {
        Intent intent = new Intent(mService, NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle extras = new Bundle();
        extras.putSerializable(SlidingPanelActivity.PANEL_STATE_KEY, SlidingPanelActivity.PanelState.EXPANDED);
        intent.putExtras(extras);

        return PendingIntent.getActivity(mService, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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
            mService.startForeground(mNotificationID, notification);
            mIsStarted = true;
        } else {
            mNotificationManager.notify(mNotificationID, notification);
        }
    }

    private Notification createNotification(MediaMetadataCompat metaData, long playingAlbumId, boolean isPlaying) {
        NotificationCompat.Builder builder = createBuilder(metaData, isPlaying);

        FakeImageView fakeImageView = new FakeImageView(mService,
                mNotificationManager, builder, mNotificationID,
                mResources.getDimensionPixelSize(R.dimen.large_icon_width),
                mResources.getDimensionPixelSize(R.dimen.large_icon_height));

        mImageLoader.load(playingAlbumId).withDefault(R.drawable.ic_notification)
                .into(fakeImageView);

        return builder.build();
    }

    private NotificationCompat.Builder createBuilder(MediaMetadataCompat metaData, boolean isPlaying) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(mService.getResources().getColor(R.color.primary))
                .setShowWhen(false) /*don't show time*/
                .setContentTitle(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                .setContentText(metaData.getString(MediaMetadataCompat.METADATA_KEY_ALBUM) +
                        " - "+ metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2 /*show all action on lock screen*/))
                .setContentIntent(mContentAction)
                .addAction(R.drawable.ic_skip_previous_white_24dp,
                        mResources.getString(R.string.notification_action_previous), mPrevAction)
                .addAction(getPlayPauseAction(isPlaying))
                .addAction(R.drawable.ic_skip_next_white_24dp,
                        mResources.getString(R.string.notification_action_next), mNextAction);
        return builder;
    }

    private Action getPlayPauseAction(boolean isPlaying) {
        int icon;
        String title;
        PendingIntent intent;
        if(isPlaying) {
            icon = R.drawable.ic_pause_white_24dp;
            title = mService.getResources().getString(R.string.notification_action_pause);
            intent = mPauseAction;
        } else {
            icon = R.drawable.ic_play_white_24dp;
            title = mService.getResources().getString(R.string.notification_action_play);
            intent = mPlayAction;
        }
        Action.Builder actionBuilder
                = new Action.Builder(icon, title, intent);
        return actionBuilder.build();
    }

    public void stopNotification() {
        if(!mIsStarted)return;

        mService.unregisterReceiver(mMediaControlActionsReceiver);
        mService.stopForeground(true);
        mNotificationManager.cancel(mNotificationID);
        mIsStarted = false;
    }

    public MediaControlActionsReceiver getControlsReceiver() {
        return mMediaControlActionsReceiver;
    }
}
