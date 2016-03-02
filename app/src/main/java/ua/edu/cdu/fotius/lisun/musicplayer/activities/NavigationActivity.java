package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.GenresFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnOpenCloseNavigationViewClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.model.ListeningLogWorker;
import ua.edu.cdu.fotius.lisun.musicplayer.sliding_panel.SlidingPanelListener;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TracksFragment;


public class NavigationActivity extends SlidingPanelActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_navigation);

        if(!isFragmentSet()) {
            setDefaultFragment(new TracksFragment(),
                    TracksFragment.TAG, getIntent().getExtras());
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = setUpNavigationView(drawerLayout);

        setTitle(getResources().getString(R.string.app_name));
        setNavigationIconResourceID(R.drawable.ic_menu_white_24dp);
        setNavigationClickListener(new OnOpenCloseNavigationViewClickListener(drawerLayout, navigationView));
        setPanelSlideListener(new SlidingPanelListener(drawerLayout, getFragmentManager()));

        //TODO: check this
        new ListeningLogWorker(this).synchronizeStorages();
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
                            case R.id.navigation_item_artists:{
                                replaceFragment(new ArtistsFragment(),
                                        ArtistsFragment.TAG);
                                break;}
                            case R.id.navigation_item_albums: {
                                replaceFragment(new AlbumsFragment(),
                                        AlbumsFragment.TAG);
                                break;}
                            case R.id.navigation_item_songs:
                                replaceFragment(new TracksFragment(),
                                        TracksFragment.TAG);
                                break;
                            case R.id.navigation_item_my_playlists:
                                replaceFragment(new PlaylistsFragment(),
                                        PlaylistsFragment.TAG);
                                break;

                            case R.id.navigation_item_genres:
                                replaceFragment(new GenresFragment(),
                                        PlaylistsFragment.TAG);
                                break;

//                            case R.id.navigation_item_settings:
//                                //TODO: initialize selectedFragment
//                                Toast.makeText(NavigationActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                                break;
                        }

                        drawer.closeDrawers();

                        menuItem.setChecked(true);
                        //true to display item as selected
                        return true;
                    }
                });
        return navigationView;
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
}
