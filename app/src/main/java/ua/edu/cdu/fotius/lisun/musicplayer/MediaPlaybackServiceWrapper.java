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

import java.util.Objects;

//TODO: all public methods should check if mBoundToService == true
/**
 * Wrapper for {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * Class is mediator between this specific application and more general
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * This class implemented as Singleton.
 */
public class MediaPlaybackServiceWrapper implements ServiceConnection {

    private final String TAG = getClass().getSimpleName();

    private static MediaPlaybackServiceWrapper instance = null;

    private boolean mBoundToService;

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

    private MediaPlaybackServiceWrapper() {
        Log.d(TAG, "MediaPlaybackServiceWrapper() called");
    }

    /**
     * Binds specified Context to
     * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}
     * @param context which binds to service
     */
    public void bindService(Context context) {
        Log.d(TAG, "Entered bindService method");
        //better to use application context, because
        //after recreating activity on configuration changes
        //context which was bind to service doesn't exists
        context = context.getApplicationContext();
        Intent service = new Intent(context, MediaPlaybackService.class);
        //if service already bounded, nothing is going to happen
        context.bindService(service, this, ContextWrapper.BIND_AUTO_CREATE);
    }

    /**
     * Unbind specified Context from
     * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}
     * @param context which unbinds from service
     */
    public void unbindService(Context context) {
        if(mBoundToService) {
            Log.e(TAG, "Entered unbindService if statement");
            context = context.getApplicationContext();
            context.unbindService(this);
            mBoundToService = false;
        }
    }

    /**
     * Play specified tracks from specified postion
     * @param cursor - tracks to play
     * @param position - first track to play
     */
    public void playAll(Cursor cursor, int position) {
        if(mBoundToService) {
            long[] playlist = getPlaylistFromCursor(cursor);
            playAll(playlist, position);
        }
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
        mBoundToService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
        mService = null;
    }
}
