package ua.edu.cdu.fotius.lisun.musicplayer;

public interface ServiceStateChangesObserver {
    public void onMetadataChanged();
    public void onPlaybackStateChanged();
}
