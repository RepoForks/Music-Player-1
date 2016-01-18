package ua.edu.cdu.fotius.lisun.musicplayer.listeners;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.QueueActivity;

public class OnShowQueueClickListener implements View.OnClickListener{



    private Context mContext;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public OnShowQueueClickListener(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, QueueActivity.class);
        mContext.startActivity(intent);
    }
}
