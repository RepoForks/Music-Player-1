package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.NowPlayingActivity;

public class OnShowQueueClickListener implements View.OnClickListener{



    private Context mContext;
    private PlaybackServiceWrapper mServiceWrapper;

    public OnShowQueueClickListener(Context context, PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, NowPlayingActivity.class);
        mContext.startActivity(intent);
    }
}
