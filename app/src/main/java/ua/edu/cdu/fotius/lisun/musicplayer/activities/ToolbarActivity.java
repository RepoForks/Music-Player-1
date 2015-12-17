package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public abstract class ToolbarActivity extends AppCompatActivity{

    private final String TAG = getClass().getSimpleName();

    protected Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
    }

    private void setUpToolbar() {
        String title = getToolbarTitle();
        int upIconResId = getNavigationIconResourceID();
        final Toolbar toolbar = mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.d(TAG, "mToolbar == null : " + (mToolbar == null));
        if(toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setNavigationIcon(upIconResId);
            setSupportActionBar(toolbar);
        }
    }

    protected abstract String getToolbarTitle();
    protected abstract int getNavigationIconResourceID();

    protected void setNavigationClickListener(View.OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
    }
}
