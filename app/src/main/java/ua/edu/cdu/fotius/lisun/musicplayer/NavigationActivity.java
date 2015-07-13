package ua.edu.cdu.fotius.lisun.musicplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments.MyPlaylistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments.SongsBrowserFragment;


public class NavigationActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private final String ARTISTS_BROWSER_FRAGMENT_TAG = "artists_browser_fragment";
    private final String ALBUMS_BROWSER_FRAGMENT_TAG = "albums_browser_fragment";
    private final String SONGS_BROWSER_FRAGMENT_TAG = "songs_browser_fragment";
    private final String MY_PLAYLISTS_BROWSER_FRAGMENT_TAG = "my_playlists_browser_fragment";

    private final String CURRENT_FRAGMENT_TAG_KEY = "current_fragment_tag";
    private Fragment mCurrentFragment;

    private String mCurrentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_navigation);

        setUpToolbarAndNavigationView();

        //if activity recreating previous state get fragment
        //which was saved on destroing previous state
        if(savedInstanceState != null) {
            mCurrentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG_KEY);
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        }

        //if activity runs for the first time set Songs fragment as
        //initial fragment
        if(mCurrentFragment == null) {
            mCurrentFragment = new SongsBrowserFragment();
            mCurrentFragmentTag = SONGS_BROWSER_FRAGMENT_TAG;
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mCurrentFragment, mCurrentFragmentTag)
                .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_FRAGMENT_TAG_KEY, mCurrentFragmentTag);
        super.onSaveInstanceState(outState);
    }

    private void setUpToolbarAndNavigationView() {
        final NavigationView navigationView =
                (NavigationView) findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                    drawer.openDrawer(navigationView);
                }
            });
        }

        //Even if toolbar will be null, we can use navigation view without it
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.navigation_item_artists:

                        //check if fragment already exsists
                        mCurrentFragment
                                = (ArtistsBrowserFragment)getSupportFragmentManager()
                                .findFragmentByTag(ARTISTS_BROWSER_FRAGMENT_TAG);

                        if(mCurrentFragment == null) {
                            mCurrentFragment = new ArtistsBrowserFragment();
                        }

                        replaceFragment(mCurrentFragment, ARTISTS_BROWSER_FRAGMENT_TAG);

                        break;
                    case R.id.navigation_item_albums:

                        mCurrentFragment
                                = (AlbumsBrowserFragment)getSupportFragmentManager()
                                .findFragmentByTag(ALBUMS_BROWSER_FRAGMENT_TAG);

                        if(mCurrentFragment == null) {
                            mCurrentFragment = new AlbumsBrowserFragment();
                        }

                        replaceFragment(mCurrentFragment, ALBUMS_BROWSER_FRAGMENT_TAG);

                        break;
                    case R.id.navigation_item_songs:
                        mCurrentFragment
                                = (AlbumsBrowserFragment)getSupportFragmentManager()
                                .findFragmentByTag(SONGS_BROWSER_FRAGMENT_TAG);

                        if(mCurrentFragment == null) {
                            mCurrentFragment = new SongsBrowserFragment();
                        }

                        replaceFragment(mCurrentFragment, SONGS_BROWSER_FRAGMENT_TAG);
                        break;
                    case R.id.navigation_item_my_playlists:
                        mCurrentFragment
                                = (AlbumsBrowserFragment)getSupportFragmentManager()
                                .findFragmentByTag(MY_PLAYLISTS_BROWSER_FRAGMENT_TAG);

                        if(mCurrentFragment == null) {
                            mCurrentFragment = new MyPlaylistsBrowserFragment();
                        }

                        replaceFragment(mCurrentFragment, MY_PLAYLISTS_BROWSER_FRAGMENT_TAG);
                        break;
                    case R.id.navigation_item_settings:
                        //TODO: call settings fragment
                        Toast.makeText(NavigationActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }

                //TODO:Debug
                Log.d(TAG, "Fragment tag:" + mCurrentFragment.getTag() + "\tFragment ID: " + mCurrentFragment.getId());

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .commit();
        mCurrentFragmentTag = fragmentTag;
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
}
