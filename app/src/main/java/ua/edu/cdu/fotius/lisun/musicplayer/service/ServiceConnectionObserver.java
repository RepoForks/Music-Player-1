package ua.edu.cdu.fotius.lisun.musicplayer.service;

/**
 * This class should be implemented by components (ex. fragments)
 * that want to receive ServiceConnection signals from {@link ua.edu.cdu.fotius.lisun.musicplayer.activities.NavigationActivity}
 */
public interface ServiceConnectionObserver {
    public void ServiceConnected();
    public void ServiceDisconnected();
}
