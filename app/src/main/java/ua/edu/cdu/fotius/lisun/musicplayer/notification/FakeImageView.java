package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageViewForLoader;


public class FakeImageView extends ImageViewForLoader{
    private int mNotificationId;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private int mWidth;
    private int mHeight;

    public FakeImageView(Context context, NotificationManager manager,
                         NotificationCompat.Builder builder, int notificationId, int width, int height) {
        super(context);
        mNotificationManager = manager;
        mNotificationId = notificationId;
        mNotificationBuilder = builder;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mNotificationBuilder.setLargeIcon(bm);
        mNotificationManager.notify(mNotificationId, mNotificationBuilder.build());
    }

    @Override
    public int getViewWidth() {
        return mWidth;
    }

    @Override
    public int getViewHeight() {
        return mHeight;
    }
}
