package ua.edu.cdu.fotius.lisun.musicplayer;

import android.database.Cursor;

/**
 * Created by andrei on 29.07.2015.
 */
public interface OnCallToServiceListener {

    public void bindToService();
    public void unbindFromService();
    public void playAll(Cursor cursor, int position);

}
