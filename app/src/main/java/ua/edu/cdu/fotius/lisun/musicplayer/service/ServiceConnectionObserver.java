package ua.edu.cdu.fotius.lisun.musicplayer.service;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.NavigationActivity;

/**
 * This class should be implemented by components (ex. fragments)
 * that want to receive ServiceConnection signals from {@link NavigationActivity}
 */
public interface ServiceConnectionObserver {
    public void ServiceConnected();
    public void ServiceDisconnected();
}
