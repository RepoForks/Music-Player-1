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
}
