package ua.edu.cdu.fotius.lisun.musicplayer.lyrics;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

    private final String KEY_LYRICS = "lyrics";

    private ToolbarActivity activity;
    @BindView(R.id.tv_lyrics)
    TextView lyricsView;
    @BindView(R.id.tv_lyrics_header)
    TextView lyricsHeaderView;

    private LyricsResponse lyrics;

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
        //retrieve only for the first time. don't on config change
        if (savedInstanceState != null) {
            lyrics = ((LyricsResponse.StateSaver)savedInstanceState.getParcelable(KEY_LYRICS)).getData();
            setUpViews(lyrics);
        }

        if (lyrics == null) {
            if (!ConnectionUtil.isConnectedToInternet(activity)) {
                activity.setResult(LyricsActivity.NO_CONNECTION_RESULT_CODE);
                activity.finish();
            } else {
                Bundle bundle = getArguments();
                String artist = bundle.getString(LyricsActivity.KEY_ARTIST);
                String song = bundle.getString(LyricsActivity.KEY_SONG);
                activity.showProgress();
                RemoteManager.lyrics(artist, song, this);
            }
        }
        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LYRICS, new LyricsResponse.StateSaver(lyrics));
    }

    @Override
    public void onLyricsSuccess(LyricsResponse response) {
        activity.hideProgress();
        lyrics = response;
        setUpViews(response);
    }

    @Override
    public void onLyricsError() {
        activity.hideProgress();
        activity.setResult(LyricsActivity.NO_LYRICS);
        activity.finish();
    }

    private void setUpViews(LyricsResponse lyrics) {
        lyricsHeaderView.setText(getString(R.string.lyrics_header_tv, lyrics.getArtist(),
                lyrics.getSong()));
        lyricsView.setText(lyrics.getLyrics());
    }
}
