package ua.edu.cdu.fotius.lisun.musicplayer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class FakeImageView extends ImageView{

    private final String TAG = getClass().getSimpleName();

    private int mNotificationId;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    public FakeImageView(Context context, NotificationManager manager,
                         NotificationCompat.Builder builder, int notificationId) {
        super(context);
        mNotificationManager = manager;
        mNotificationId = notificationId;
        mNotificationBuilder = builder;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mNotificationBuilder.setLargeIcon(bm);
        Log.d(TAG, "setImage-----");
        mNotificationManager.notify(mNotificationId, mNotificationBuilder.build());
    }
}
