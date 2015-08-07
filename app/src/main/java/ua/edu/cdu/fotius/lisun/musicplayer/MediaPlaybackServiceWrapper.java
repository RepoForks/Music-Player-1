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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**Wrapper for {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * Class is mediator between this specific application and more general
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * This class implemented as Singleton.
 * Also some class methods are written to perform one operation - handle exception.*/
// TODO: проблема может быть в следующем:
// При config changes этот класс может быть уничтожен
// и перевоссоздан, таким образом потеряем данные об обзёрверах
// Чтобы предотвратить это нужно либо реализовать паттерн Singleton, либо
// обьявить класс как public static где-нибудь во фрагменте и пользоваться им в Activity
// НО ОБЕСПЕЧИВАЮТ ЛИ static ссылки в этих 2-х случаях стойкость обьекта к config changes?
// Или class loader может в какой момент быть рассматриваться как "мусор"?
// Запасным и не очень желательным вариантом есть инициализация данного объекта в ApplicationContext.
// Еще неудачный вариант: использование onRetainNonConfigurationInstance(), который deprecated.
public class MediaPlaybackServiceWrapper
        implements ServiceConnection{

    private final String TAG = getClass().getSimpleName();

    private static MediaPlaybackServiceWrapper instance = null;

    public static MediaPlaybackServiceWrapper getInstance() {
        if (instance == null) {
            instance = new MediaPlaybackServiceWrapper();
        }
        return instance;
    }

    /*Need to use thread-safe collection.
    * Async danger: bindService(...) writes to collection
    * and onServiceConnection(...) reads collection elements*/
    private Vector<ServiceConnectionObserver> mConnectionObservers =
            new Vector<ServiceConnectionObserver>();

    private IMediaPlaybackService mService = null;

    private MediaPlaybackServiceWrapper(){}

    public void bindService(Context context, ServiceConnectionObserver connectionObserver) {
        /*Adding observer should be the first operation in this function*/
        mConnectionObservers.add(connectionObserver);

        //service hasn't been connected yet
        if(mService == null) {
            /*Better to use application context, because
            * after recreating activity on configuration changes
            * activity context which was bind to service doesn't exists*/
            context = context.getApplicationContext();
            Intent service = new Intent(context, MediaPlaybackService.class);
            context.bindService(service, this, ContextWrapper.BIND_AUTO_CREATE);
        }
    }

    public void unbindService(Context context, ServiceConnectionObserver connectionObserver) {
        ServiceConnectionObserver candidateForUnbinding = connectionObserver;
        mConnectionObservers.remove(candidateForUnbinding);

        if (mConnectionObservers.size() == 0) {
            context = context.getApplicationContext();
            context.unbindService(this);
            mService = null;
        }
    }

    public void playAll(Cursor cursor, int position) {
        if (mService != null) {
            long[] playlist = getPlaylistFromCursor(cursor);
            playAll(playlist, position);
        }
    }

    private void playAll(long[] playlist, int position) {
        try {
            mService.open(playlist, position);
            mService.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private long[] getPlaylistFromCursor(Cursor cursor) {
        int cursorLinesQuantity = cursor.getCount();
        long[] playlist = new long[cursorLinesQuantity];
        cursor.moveToFirst();

        int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);

        for (int i = 0; i < cursorLinesQuantity; i++) {
            playlist[i] = cursor.getLong(idColumnIndex);
            cursor.moveToNext();
        }
        return playlist;
    }

    public void prev() {
        if (mService != null) {
            try {
                mService.prev();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public void seek(long position) {
        if (mService != null) {
            try {
                mService.seek(position);
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public void next() {
        if (mService != null) {
            try {
                mService.next();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public void play() {
        if (mService != null) {
            try {
                mService.play();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public long getTrackDuration() {
        if (mService != null) {
            try {
                return mService.duration();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return -1;
    }

    public long getPlayingPosition() {
        if (mService != null) {
            try {
                return mService.position();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return -1;
    }

    public String getArtistName() {
        if (mService != null) {
            try {
                return mService.getArtistName();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return null;
    }

    public String getTrackName() {
        if (mService != null) {
            try {
                return mService.getTrackName();
            } catch (RemoteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IMediaPlaybackService.Stub.asInterface(service);
        /* Notify all observers.
         * In most cases here will be notified
         * one observer, which is added to collection first.
         * But sometimes binding can delays and in this case
         * more than one observer can be notified */
        for(ServiceConnectionObserver observer : mConnectionObservers) {
            observer.ServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        for(ServiceConnectionObserver observer : mConnectionObservers) {
            observer.ServiceDisconnected();
        }
        mService = null;
    }
}
