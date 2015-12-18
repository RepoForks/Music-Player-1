package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;

public class AlbumsDetalizationActivity extends SlidingPanelActivity implements ToolbarStateListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton);

        if(!isFragmentSet()) {
            setDefaultFragment(new AlbumsBrowserFragment(),
                    AlbumsBrowserFragment.TAG, getIntent().getExtras());
        }

        setTitle( getIntent().getExtras(),
                getResources().getString(R.string.default_albums_detalization_activity_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
        setPanelSlideListener(new SlidingPanelListener(null));
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
