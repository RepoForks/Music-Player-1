package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment.PlaybackFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;


public class NavigationActivity extends BaseActivity implements ToolbarStateListener {

    private final String TAG = getClass().getSimpleName();

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private Fragment mCurrentBrowserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_navigation);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = setUpNavigationView(drawerLayout);

        String toolbarTitle =
                getToolbarTitle(null, getResources().getString(R.string.app_name));
        setUpToolbar(R.id.toolbar,
                toolbarTitle,
                R.drawable.ic_menu_black_24dp,
                new OnOpenCloseNavigationViewClickListener(drawerLayout, navigationView));
        setUpSlidingPanel(R.id.sliding_up_panel_layout, drawerLayout);

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
                                replaceFragment(new PlaylistsBrowserFragment(),
                                        PlaylistsBrowserFragment.TAG);
                                break;
                            case R.id.navigation_item_settings:
                                //TODO: initialize selectedFragment
                                Toast.makeText(NavigationActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        drawer.closeDrawers();

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

    @Override
    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }
}
