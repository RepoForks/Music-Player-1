package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.NowPlayingFragment;

public class NowPlayingActivity extends ToolbarActivity implements ToolbarStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        if(!isFragmentSet()) {
            setDefaultFragment(new NowPlayingFragment(),
                    NowPlayingFragment.TAG, getIntent().getExtras());
        }
    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void hideToolbar() {

    }
}
