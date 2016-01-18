package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.QueueFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnUpClickListener;

public class QueueActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        if(!isFragmentSet()) {
            setDefaultFragment(new QueueFragment(),
                    QueueFragment.TAG, getIntent().getExtras());
        }

        setTitle(getResources().getString(R.string.now_playing_activity_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_black_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
    }
}
