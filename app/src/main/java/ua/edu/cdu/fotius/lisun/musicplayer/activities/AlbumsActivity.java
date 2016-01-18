package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnUpClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.sliding_panel.SlidingPanelListener;

public class AlbumsActivity extends SlidingPanelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_panel);

        if(!isFragmentSet()) {
            setDefaultFragment(new AlbumsFragment(),
                    AlbumsFragment.TAG, getIntent().getExtras());
        }

        setTitle( getIntent().getExtras(),
                getResources().getString(R.string.default_albums_detalization_activity_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
        setPanelSlideListener(new SlidingPanelListener(null, getSupportFragmentManager()));
    }
}
