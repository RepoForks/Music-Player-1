package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public interface ListenerCallbacks {
    public void play();
    public void pause();
    public boolean isPlaying();
    public void goToNextTrack();
    public void goToPreviousTrack();
    public void seek(long position);
    public long getPlayingPosition();
    public long getTrackDuration();
    public void setRepeatMode(int repeatMode);
    public int getRepeatMode();
    public void setShuffleMode(int shuffleMode);
    public int getShuffleMode();
    public void setRepeatButtonImageCallback();
    public void setShuffleButtonImageCallback();
    public void setPlayPauseButtonsImageCallback();
    public long refreshSeekBarAndCurrentTimeCallback();
}
