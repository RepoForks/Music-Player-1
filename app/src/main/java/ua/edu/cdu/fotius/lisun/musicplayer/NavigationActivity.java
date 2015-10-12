package ua.edu.cdu.fotius.lisun.musicplayer;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.UsersPlaylistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;


public class NavigationActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private Fragment mCurrentBrowserFragment;

    private DrawerLayout mDrawerLayout;
    private SlidingUpPanelLayout mSlidingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_navigation);

        mSlidingPanel = (SlidingUpPanelLayout)findViewById(R.id.sliding_up_panel_layout);
        mSlidingPanel.setPanelSlideListener(mSlidingPanelListener);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = setUpNavigationView(mDrawerLayout);
        setUpToolbar(mDrawerLayout, navigationView);

        //if activity recreating previous state get fragment
        //which was saved on destroing previous state
        if(savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentBrowserFragment = getSupportFragmentManager()
                    .findFragmentByTag(savedFragmentTag);
        }

        //if activity runs for the first time set Songs fragment as
        //initial fragment
        if(mCurrentBrowserFragment == null) {
            Fragment fragment = new TrackBrowserFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, TrackBrowserFragment.TAG)
                .commit();
            mCurrentBrowserFragment = fragment;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentBrowserFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    private NavigationView setUpNavigationView(final DrawerLayout drawer) {
        final NavigationView navigationView =
                (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //if tending to replace with the same fragment don't do this, because this will call
                        //Fragment#OnCreate() where database time consuming operations are performed
                        switch (menuItem.getItemId()){
                            case R.id.navigation_item_artists:
                                replaceFragment(new ArtistsBrowserFragment(),
                                        ArtistsBrowserFragment.TAG);
                                break;
                            case R.id.navigation_item_albums:
                                replaceFragment(new AlbumsBrowserFragment(),
                                        AlbumsBrowserFragment.TAG);
                                break;
                            case R.id.navigation_item_songs:
                                replaceFragment(new TrackBrowserFragment(),
                                        TrackBrowserFragment.TAG);
                                break;
                            case R.id.navigation_item_my_playlists:
                                replaceFragment(new UsersPlaylistsBrowserFragment(),
                                        UsersPlaylistsBrowserFragment.TAG);
                                break;
                            case R.id.navigation_item_settings:
                                //TODO: initialize selectedFragment
                                Toast.makeText(NavigationActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        drawer.closeDrawers();

                        //TODO: when rotating selected checking is disappeared
                        menuItem.setChecked(true);
                        //true to display item as selected
                        return true;
                    }
                });
        return navigationView;
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .commit();
        mCurrentBrowserFragment = fragment;
    }

    private Toolbar setUpToolbar(final DrawerLayout drawer, final NavigationView navigationView) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(navigationView);
                }
            });
        }
        return toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private SlidingUpPanelLayout.PanelSlideListener mSlidingPanelListener = new SlidingUpPanelLayout.PanelSlideListener() {

        private LinearLayout mAdditionalControlPanel;
        private boolean mIsAdditionalControlsVisible;

        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            if ((slideOffset > 0.6) && mIsAdditionalControlsVisible) {
                mAdditionalControlPanel =
                        (LinearLayout) panel.findViewById(R.id.additional_control_panel);
                mAdditionalControlPanel.setVisibility(View.GONE);
                mIsAdditionalControlsVisible = false;
            }

            if ((slideOffset < 0.6) && !mIsAdditionalControlsVisible) {
                mAdditionalControlPanel =
                        (LinearLayout) panel.findViewById(R.id.additional_control_panel);

                mAdditionalControlPanel.setVisibility(View.VISIBLE);
                mIsAdditionalControlsVisible = true;
            }
        }

        @Override
        public void onPanelCollapsed(View panel) {
            //enable ability of opening navigation menu
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        @Override
        public void onPanelExpanded(View panel) {
            //disable ability of opening navigation menu
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        @Override
        public void onPanelAnchored(View panel) {}
        @Override
        public void onPanelHidden(View panel) {}
    };
}
