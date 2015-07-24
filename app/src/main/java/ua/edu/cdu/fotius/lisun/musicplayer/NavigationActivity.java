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
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.MyPlaylistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.OnFragmentReplaceListener;


public class NavigationActivity extends AppCompatActivity implements OnFragmentReplaceListener {

    private final String TAG = getClass().getSimpleName();

    //TODO: All these tags to their fragments
    public static final String TRACK_BROWSER_FRAGMENT_TAG = "track_browser_fragment";
    private final String ARTISTS_BROWSER_FRAGMENT_TAG = "artists_browser_fragment";
    public static final String ALBUMS_BROWSER_FRAGMENT_TAG = "albums_browser_fragment";
    private final String MY_PLAYLISTS_BROWSER_FRAGMENT_TAG = "my_playlists_browser_fragment";

    private final String CURRENT_FRAGMENT_TAG_KEY = "current_fragment_tag";
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_navigation);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = setUpNavigationView(drawer);
        setUpToolbar(drawer, navigationView);
        //setUpToolbarAndNavigationView();

        //if activity recreating previous state get fragment
        //which was saved on destroing previous state
        if(savedInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager()
                    .findFragmentByTag(savedInstanceState.getString(CURRENT_FRAGMENT_TAG_KEY));
        }

        //if activity runs for the first time set Songs fragment as
        //initial fragment
        if(mCurrentFragment == null) {
            mCurrentFragment = new TrackBrowserFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mCurrentFragment, TRACK_BROWSER_FRAGMENT_TAG)
                .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_FRAGMENT_TAG_KEY, mCurrentFragment.getTag());
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
                                if(!isSameFragment(ARTISTS_BROWSER_FRAGMENT_TAG)) {
                                    replaceFragment(new ArtistsBrowserFragment(),
                                            ARTISTS_BROWSER_FRAGMENT_TAG);
                                }
                                break;
                            case R.id.navigation_item_albums:
                                if(!isSameFragment(ALBUMS_BROWSER_FRAGMENT_TAG)) {
                                    replaceFragment(new AlbumsBrowserFragment(),
                                            ALBUMS_BROWSER_FRAGMENT_TAG);
                                }
                                break;
                            case R.id.navigation_item_songs:
                                if(!isSameFragment(TRACK_BROWSER_FRAGMENT_TAG)) {
                                    replaceFragment(new TrackBrowserFragment(),
                                            TRACK_BROWSER_FRAGMENT_TAG);
                                }
                                break;
                            case R.id.navigation_item_my_playlists:
                                if(!isSameFragment(MY_PLAYLISTS_BROWSER_FRAGMENT_TAG)) {
                                    replaceFragment(new MyPlaylistsBrowserFragment(),
                                            MY_PLAYLISTS_BROWSER_FRAGMENT_TAG);
                                }
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

                    private boolean isSameFragment(String fragmentTag) {
                        if((mCurrentFragment.getTag()).equals(fragmentTag)) {
                            return true;
                        }
                        return false;
                    }
                });
        return navigationView;
    }

    private Toolbar setUpToolbar(final DrawerLayout drawer, final NavigationView navigationView) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Replacing fragments here, because need to track
     * which fragment in foreground and active now
     * @param fragment - fragment which will replace current
     * @param fragmentTag - tag of new fragment
     */
    @Override
    public void replaceFragment(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .commit();
        mCurrentFragment = fragment;
    }
}
