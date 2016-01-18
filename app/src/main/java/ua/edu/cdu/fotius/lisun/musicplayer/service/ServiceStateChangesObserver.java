package ua.edu.cdu.fotius.lisun.musicplayer.service;

public interface ServiceStateChangesObserver {
    public void onMetadataChanged();
    public void onPlaybackStateChanged();
}
