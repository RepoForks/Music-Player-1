package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public interface PlaybackViewsStateListener {
    public long updateSeekBarAndCurrentTime();
    public void updatePlayPauseButtonImage();
    public void updateRepeatButtonImage();
    public void updateShuffleButtonImage();
}
