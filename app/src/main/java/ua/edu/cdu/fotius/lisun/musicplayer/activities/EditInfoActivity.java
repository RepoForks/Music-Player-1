package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.EditTrackInfoFragment;

public class EditInfoActivity extends BaseActivity {

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

        if(savedInstanceState == null) {
            EditTrackInfoFragment editTrackInfoFragment = new EditTrackInfoFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, editTrackInfoFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
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
}
