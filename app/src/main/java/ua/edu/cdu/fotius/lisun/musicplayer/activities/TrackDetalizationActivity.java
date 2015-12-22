package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;


public class TrackDetalizationActivity extends SlidingPanelActivity implements ToolbarStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_panel);

        if (!isFragmentSet()) {
            setDefaultFragment(new TrackBrowserFragment(),
                    TrackBrowserFragment.TAG, getIntent().getExtras());
        }

        setTitle(getIntent().getExtras(),
                getResources().getString(R.string.default_track_detalization_activity_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
        setPanelSlideListener(new SlidingPanelListener(null, getSupportFragmentManager()));
    }

    @Override
    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }
}
