package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.OnUpClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class EditInfoActivity extends ToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        if(!isFragmentSet()) {
            setDefaultFragment(new EditTrackInfoFragment(),
                    EditTrackInfoFragment.TAG, getIntent().getExtras());
        }

        setTitle(getResources().getString(R.string.default_edit_info_activity));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
        return true;
    }

    //TODO: make button for this
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.done_editing) {
//            if(mCurrentFragment != null) {
//                mCurrentFragment.doneEditing();
//            }
//            return true;
//        } else if(id == R.id.cancel_editing) {
//            Log.d(TAG, "Cancel");
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
