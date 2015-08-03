package ua.edu.cdu.fotius.lisun.musicplayer;

import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
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

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.MyPlaylistsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.SlidingUpPanelLayout;


public class NavigationActivity extends AppCompatActivity
        implements OnFragmentReplaceListener, OnCallToServiceListener{

    private final String TAG = getClass().getSimpleName();

    private final String CURRENT_FRAGMENT_TAG_KEY = "current_fragment_tag";
    private Fragment mCurrentFragment;

    private DrawerLayout mDrawerLayout;
    private SlidingUpPanelLayout mSlidingPanel;
    private MediaPlaybackServiceWrapper mServiceWrapper
            = MediaPlaybackServiceWrapper.getInstance();

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
            mCurrentFragment = getSupportFragmentManager()
                    .findFragmentByTag(savedInstanceState.getString(CURRENT_FRAGMENT_TAG_KEY));
        }

        //if activity runs for the first time set Songs fragment as
        //initial fragment
        if(mCurrentFragment == null) {
            mCurrentFragment = new TrackBrowserFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mCurrentFragment, TrackBrowserFragment.TAG)
                .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                                replaceFragment(new MyPlaylistsBrowserFragment(),
                                        MyPlaylistsBrowserFragment.TAG);
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

    @Override
    public void bindToService() {
        mServiceWrapper.bindService(this);
    }

    @Override
    public void unbindFromService() {
        mServiceWrapper.unbindService(this);
    }

    //TODO: if service unbind for this context finish app and
    //let the user restart it
    @Override
    public void playAll(Cursor cursor, int position) {
        mServiceWrapper.playAll(cursor, position);
    }

    @Override
    public long position() throws RemoteException {
         return mServiceWrapper.position();
    }

    @Override
    public void prev() throws RemoteException {
        mServiceWrapper.prev();
    }

    @Override
    public void seek(long position) throws RemoteException {
        mServiceWrapper.seek(position);
    }

    @Override
    public void next() throws RemoteException {
        mServiceWrapper.next();
    }

    @Override
    public void play() throws RemoteException {
        mServiceWrapper.play();
    }


    private SlidingUpPanelLayout.PanelSlideListener mSlidingPanelListener = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            Log.d(TAG, "slideOffset: " + slideOffset);
            if(slideOffset == 0.6) {
                //TODO: disable upper control buttons
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
