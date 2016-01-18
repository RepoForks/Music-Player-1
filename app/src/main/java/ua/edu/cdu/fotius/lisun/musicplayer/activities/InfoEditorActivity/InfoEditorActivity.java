package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.InfoEditorFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnUpClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class InfoEditorActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        if(!isFragmentSet()) {
            setDefaultFragment(new InfoEditorFragment(),
                    InfoEditorFragment.TAG, getIntent().getExtras());
        }

        setTitle(getResources().getString(R.string.default_edit_info_activity));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
    }
}
