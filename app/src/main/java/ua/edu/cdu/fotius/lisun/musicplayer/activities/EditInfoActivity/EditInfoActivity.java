package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.OnUpClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class EditInfoActivity extends ToolbarActivity {

    private final String TAG = getClass().getSimpleName();

    private final String EXTRA_FRAGMENT_TAG = "extra_fragment_tag";
    private EditTrackInfoFragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        setTitle(getResources().getString(R.string.default_edit_info_activity));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));

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
            if(mCurrentFragment != null) {
                mCurrentFragment.doneEditing();
            }
            return true;
        } else if(id == R.id.cancel_editing) {
            Log.d(TAG, "Cancel");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
