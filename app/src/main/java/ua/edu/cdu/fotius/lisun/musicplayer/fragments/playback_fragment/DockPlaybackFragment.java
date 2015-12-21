package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ConcealableImageView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.LoopingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;


//TODO: setRetainInstance
public class DockPlaybackFragment extends Fragment implements ServiceConnectionObserver,
        OnRewindListener.RewindClickedListener, OnForwardListener.ForwardClickedListener {


    private final String TAG = getClass().getSimpleName();

    private MediaPlaybackServiceWrapper mServiceWrapper;

    private ImageView mAlbumArt;
    private SeekBar mSeekBar;
    private TextView mTrackTitle;
    private TextView mTrackArtist;

    public DockPlaybackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dock_playback, container, false);

        LoopingImageButton prev = (LoopingImageButton) v.findViewById(R.id.prev_additional);
        prev.setOnClickListener(new OnPreviousClickListener(mServiceWrapper));
        prev.setRepeatListener(new OnRewindListener(mServiceWrapper, this));

        LoopingImageButton next =
                (LoopingImageButton) v.findViewById(R.id.next_additional);
        next.setOnClickListener(new OnNextClickListener(mServiceWrapper));
        next.setRepeatListener(new OnForwardListener(mServiceWrapper, this));


        PlayPauseButton play = (PlayPauseButton) v.findViewById(R.id.play_additional);
        play.setOnClickListener(new OnPlayPauseClickListener(mServiceWrapper));

        mAlbumArt = (ImageView) v.findViewById(R.id.album_art);

        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mTrackArtist = (TextView) v.findViewById(R.id.artist_name);

        mSeekBar = (SeekBar) v.findViewById(R.id.seek_bar);
        mSeekBar.setMax(OnSeekBarChangeListener.SEEK_BAR_MAX);

        return v;
    }


    @Override
    public void ServiceConnected() {

    }

    @Override
    public void ServiceDisconnected() {

    }

    @Override
    public void onForwardClicked() {
        Log.d(TAG, "Small forward clicked");
    }

    @Override
    public void onRewindClicked() {
        Log.d(TAG, "Small rewind clicked");
    }
}
