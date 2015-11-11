package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.ToolbarStateListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseLoaderFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.PlaylistTracksBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AlbumTracksBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;


public class TrackDetalizationActivity extends AppCompatActivity implements ToolbarStateListener {

    private final String TAG = getClass().getSimpleName();

    public static final String CALLED_FROM_KEY = "from_key";
    public static final int CALLED_FROM_ALBUMS = 1;
    public static final int CALLED_FROM_PLAYLIST = 2;

    private final String EXTRA_FRAGMENT_TAG = "current_fragment_tag";

    private Fragment mCurrentFragment;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton);

        mToolbar = setUpToolbar();

        SlidingUpPanelLayout slidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_up_panel_layout);
        slidingPanel.setPanelSlideListener(new SlidingPanelListener(null));

        if (savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(savedFragmentTag);
        }

        if (mCurrentFragment == null) {
            BaseLoaderFragment fragment = getFragment(getIntent());
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, fragment.getFragmentTag()).commit();
            mCurrentFragment = fragment;
        }
    }

    private BaseLoaderFragment getFragment(Intent intent) {
        BaseLoaderFragment baseFragment = null;
        Bundle extras = null;
        String tag = null;
        if (intent != null) {
            extras = intent.getExtras();
            int from = extras.getInt(CALLED_FROM_KEY);
            switch (from) {
                case CALLED_FROM_ALBUMS:
                    baseFragment = new AlbumTracksBrowserFragment();
                    tag = AlbumTracksBrowserFragment.TAG;
                    break;
                case CALLED_FROM_PLAYLIST:
                    baseFragment = new PlaylistTracksBrowserFragment();
                    tag = PlaylistTracksBrowserFragment.TAG;
                    break;
                /*idelly won't be called at all, but needed for emergency*/
                default:
                    baseFragment = new TrackBrowserFragment();
                    tag = TrackBrowserFragment.TAG;
                    break;
            }
        }
        Log.d(TAG, "TAG: " + tag);
        baseFragment.setFragmentTag(tag);
        baseFragment.setArguments(extras);
        return baseFragment;
    }

    private Toolbar setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return toolbar;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentFragment.getTag());
        super.onSaveInstanceState(outState);
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
