package ua.edu.cdu.fotius.lisun.musicplayer.lyrics;

import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Retrofit;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.QueueFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnUpClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote.LyricsResponse;
import ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote.RemoteManager;

public class LyricsActivity extends ToolbarActivity {

    public static final String KEY_ARTIST = "artist";
    public static final String KEY_SONG = "song";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        if(!isFragmentSet()) {
            setDefaultFragment(new LyricsFragment(),
                    LyricsFragment.class.getName(), getIntent().getExtras());
        }

        setTitle(getResources().getString(R.string.lyrics_activity_title));
        setNavigationIconResourceID(R.drawable.ic_arrow_back_white_24dp);
        setNavigationClickListener(new OnUpClickListener(this));
    }
}
