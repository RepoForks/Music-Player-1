package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.os.RemoteException;

public interface ServiceInterface {

    public void bindToService(Context context, ServiceConnectionObserver connectionObserver);
    public void unbindFromService(Context context, ServiceConnectionObserver connectionObserver);

    public void playAll(Cursor cursor, int position);

    public void prev();

    public void seek(long position);

    public void next();

    public void play();

    public void pause();

    public boolean isPlaying();

    public String getTrackName();
    public String getArtistName();
    //TODO: seek -> playing
    public long getPlayingPosition();
    public long getTrackDuration();

    public void setRepeatMode(int repeatMode);
    public int getRepeatMode();

    public void setShuffleMode(int shuffleMode);
    public int getShuffleMode();
}
