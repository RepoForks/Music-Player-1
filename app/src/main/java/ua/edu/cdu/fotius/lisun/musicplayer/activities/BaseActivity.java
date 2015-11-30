package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TOOLBAR_TITLE_KEY = "title_key";

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUpSlidingPanel(final int panelResId, final DrawerLayout drawerLayout) {
        SlidingUpPanelLayout slidingPanel = (SlidingUpPanelLayout)findViewById(panelResId);
        slidingPanel.setPanelSlideListener(new SlidingPanelListener(drawerLayout));
    }

    public Toolbar setUpToolbar(final int toolbarResId, final String title,
                                final int upIconResId, final View.OnClickListener onUpClickListener) {
        final Toolbar toolbar = mToolbar = (Toolbar) findViewById(toolbarResId);
        if(toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setNavigationIcon(upIconResId);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(onUpClickListener);
        }
        return toolbar;
    }

    public String getToolbarTitle(Bundle extras, String defaultString) {
        if(defaultString == null) {
            return getResources().getString(R.string.app_name);
        }

        if(extras == null) {
            return defaultString;
        }

        String title = extras.getString(TOOLBAR_TITLE_KEY);
        if(title == null) {
            title = defaultString;
        }
        return title;
    }
}
