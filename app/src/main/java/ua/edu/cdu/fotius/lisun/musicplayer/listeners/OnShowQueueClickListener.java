package ua.edu.cdu.fotius.lisun.musicplayer.listeners;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.QueueActivity;

public class OnShowQueueClickListener implements View.OnClickListener{
    private Context mContext;

    public OnShowQueueClickListener(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, QueueActivity.class);
        mContext.startActivity(intent);
    }
}
