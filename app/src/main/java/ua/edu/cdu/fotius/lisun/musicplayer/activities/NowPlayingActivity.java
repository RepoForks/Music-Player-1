package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.NowPlayingFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;

public class NowPlayingActivity extends AppCompatActivity implements ToolbarStateListener{

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";

    private Fragment mCurrentBrowserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        if(savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentBrowserFragment = getSupportFragmentManager()
                    .findFragmentByTag(savedFragmentTag);
        }

        //if activity runs for the first time set Songs fragment as
        //initial fragment
        if(mCurrentBrowserFragment == null) {
            Fragment fragment = new NowPlayingFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, NowPlayingFragment.TAG)
                    .commit();
            mCurrentBrowserFragment = fragment;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentBrowserFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_now_playing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void hideToolbar() {

    }
}
