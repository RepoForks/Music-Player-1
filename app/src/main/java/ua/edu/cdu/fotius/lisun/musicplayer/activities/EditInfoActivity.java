package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.EditTrackInfoFragment;

public class EditInfoActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private EditTrackInfoFragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton);

        String toolbarTitle = getToolbarTitle(
                null,
                getResources().getString(R.string.default_edit_info_activity));
        setUpToolbar(R.id.toolbar,
                toolbarTitle,
                R.mipmap.ic_arrow_back_black_24dp,
                new OnUpClickListener(this));
        setUpSlidingPanel(R.id.sliding_up_panel_layout, null);

        if(savedInstanceState != null) {
            String savedFragmentTag = savedInstanceState.getString(EXTRA_FRAGMENT_TAG);
            mCurrentFragment = (EditTrackInfoFragment)getSupportFragmentManager()
                    .findFragmentByTag(savedFragmentTag);
        }

        if(mCurrentFragment == null) {
            EditTrackInfoFragment editTrackInfoFragment = new EditTrackInfoFragment();
            editTrackInfoFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    editTrackInfoFragment, EditTrackInfoFragment.TAG).commit();
            mCurrentFragment = editTrackInfoFragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FRAGMENT_TAG, mCurrentFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.done_editing) {
            Log.d(TAG, "Done");
            return true;
        } else if(id == R.id.cancel_editing) {
            Log.d(TAG, "Cancel");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
