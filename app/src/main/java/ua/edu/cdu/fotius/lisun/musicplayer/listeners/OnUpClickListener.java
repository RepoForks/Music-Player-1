package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class OnUpClickListener implements View.OnClickListener{

    private ToolbarActivity mActivity;

    public OnUpClickListener(ToolbarActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        mActivity.finish();
    }
}
