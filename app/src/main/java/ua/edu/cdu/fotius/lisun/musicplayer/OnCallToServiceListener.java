package ua.edu.cdu.fotius.lisun.musicplayer;

import android.database.Cursor;
import android.os.RemoteException;

/**
 * Created by andrei on 29.07.2015.
 */
public interface OnCallToServiceListener {

    public void bindToService();

    public boolean isBoundToService();

    public void unbindFromService();

    public void playAll(Cursor cursor, int position);

    public void prev();

    public void seek(long position);

    public void next();

    public void play();

    public String getTrackName();
    public String getArtistName();
    //TODO: seek -> playing
    public long getPlayingPosition();
    public long getTrackDuration();
}
