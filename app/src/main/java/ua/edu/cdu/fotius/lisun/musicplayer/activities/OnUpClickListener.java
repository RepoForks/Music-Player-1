package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class OnUpClickListener implements View.OnClickListener{

    private BaseActivity mActivity;

    public OnUpClickListener(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        mActivity.finish();
    }
}