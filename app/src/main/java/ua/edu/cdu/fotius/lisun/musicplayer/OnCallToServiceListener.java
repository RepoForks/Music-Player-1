package ua.edu.cdu.fotius.lisun.musicplayer;

import android.database.Cursor;
import android.os.RemoteException;

/**
 * Created by andrei on 29.07.2015.
 */
public interface OnCallToServiceListener {

    public void bindToService();
    public void unbindFromService();
    public void playAll(Cursor cursor, int position);
    public long position() throws RemoteException;
    public void prev() throws RemoteException;
    public void seek(long position) throws RemoteException;
    public void next() throws RemoteException;
    public void play() throws RemoteException;
}
