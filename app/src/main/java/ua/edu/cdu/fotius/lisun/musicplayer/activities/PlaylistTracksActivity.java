package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.os.Bundle;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistTracksFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnUpClickListener;

public class PlaylistTracksActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        if(!isFragmentSet()) {
            setDefaultFragment(new PlaylistTracksFragment(),
                    PlaylistTracksFragment.TAG, getIntent().getExtras());
        }

        //TODO: there is no need to set default cause it is set in manifest.
        // Better to throw exception when there is no custom title
        setTitle(getIntent().getExtras(), getResources()
                .getString(R.string.playlist_tracklist_default_toolbar_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_white_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
    }
}
