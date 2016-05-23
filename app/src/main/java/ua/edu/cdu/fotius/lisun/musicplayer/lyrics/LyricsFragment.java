package ua.edu.cdu.fotius.lisun.musicplayer.lyrics;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote.LyricsResponse;
import ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote.RemoteManager;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.ConnectionUtil;

public class LyricsFragment extends Fragment implements RemoteManager.OnLyricsRetrievedListener {

    private ToolbarActivity activity;
    @BindView(R.id.tv_lyrics)
    TextView lyricsView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ToolbarActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lyrics, container, false);
        ButterKnife.bind(this, v);

//        if(!ConnectionUtil.isConnectedToWifi(activity)) {
//            return v;
//        }
        //retrieve only for the first time. don't on config change
        if(savedInstanceState == null) {
            Bundle bundle = getArguments();
            String artist = bundle.getString(LyricsActivity.KEY_ARTIST);
            String song = bundle.getString(LyricsActivity.KEY_SONG);
            Log.d(LyricsActivity.class.getSimpleName(), "onCreate. Artist: " + artist +
                    " Song: " + song);
            activity.showProgress();
            RemoteManager.lyrics(artist, song, this);
        }
        return v;
    }

    @Override
    public void onLyricsSuccess(LyricsResponse response) {
        activity.hideProgress();
        lyricsView.setText(getString(R.string.lyrics_tv, response.getArtist(),
                response.getSong(), response.getLyrics()));
    }

    @Override
    public void onLyricsError() {
        activity.hideProgress();
        Toast.makeText(activity, R.string.lyrics_loading_error, Toast.LENGTH_LONG);
    }
}
