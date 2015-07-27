package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Wrapper for {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * Class is mediator between this specific application and more general
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * This class implemented as Singleton.
 */
public class MediaPlaybackServiceWrapper implements ServiceConnection {

    private final String TAG = getClass().getSimpleName();

    private static MediaPlaybackServiceWrapper instance = null;

    /**
     * @return instance of
     * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper}
     */
    public static MediaPlaybackServiceWrapper getInstance() {
        if(instance == null) {
            instance = new MediaPlaybackServiceWrapper();
        }
        return instance;
    }

    private IMediaPlaybackService mService = null;

    private MediaPlaybackServiceWrapper() {}

    /**
     * Binds specified Context to
     * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}
     * @param context which binds to service
     */
    public void bindService(Context context) {
        Intent service = new Intent(context, MediaPlaybackService.class);
        Log.d(TAG, "bind service");
        context.bindService(service, this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind specified Context from
     * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}
     * @param context which unbinds from service
     */
    public void unbindService(Context context) {
        Log.d(TAG, "unbind service");
        context.unbindService(this);
    }

    /**
     * Play specified tracks from specified postion
     * @param cursor - tracks to play
     * @param position - first track to play
     */
    public void playAll(Cursor cursor, int position) {
        long[] playlist = getPlaylistFromCursor(cursor);
        playAll(playlist, position);
    }

    private void playAll(long[] playlist, int position) {
        Log.d(TAG, "playAll: position " + position);
        try {
            mService.open(playlist, position);
            mService.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private long[] getPlaylistFromCursor(Cursor cursor) {
        //get cursor lines quantity
        int tracksQty = cursor.getCount();
        //create list for playlist id's
        long[] playlist = new long[tracksQty];
        //move to first track in cursor
        cursor.moveToFirst();
        //get song id column index
        int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        //go through cursor
        for(int i = 0; i < tracksQty; i++) {
            //retreive track id
            //put id to list
            playlist[i] = cursor.getLong(idColumnIndex);
            //cursor to next item
            cursor.moveToNext();
        }
        //return result
        return playlist;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected");
        mService = IMediaPlaybackService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
        mService = null;
    }
}
