package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Vector;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.IMediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;

/**
 * Wrapper for {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * Class is mediator between this specific application and more general
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService}.
 * This class implemented as Singleton.
 * Also some class methods are written to perform one operation - handle exception.
 */
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
        implements ServiceConnection {

    private final String TAG = getClass().getSimpleName();

    //TODO: maybe its AudioStorage.WRONG_ID
    public static int ERROR_RETURN_VALUE = -1;


    private static MediaPlaybackServiceWrapper instance = null;

    public static MediaPlaybackServiceWrapper getInstance() {
        if (instance == null) {
            instance = new MediaPlaybackServiceWrapper();
        }
        return instance;
    }

    /*Need to use thread-safe collection.
    * Async danger: bindToService(...) writes to collection
    * and onServiceConnection(...) reads collection elements*/
    private Vector<ServiceConnectionObserver> mConnectionObservers =
            new Vector<ServiceConnectionObserver>();

    private IMediaPlaybackService mService = null;

    private MediaPlaybackServiceWrapper() {
    }

    public void bindToService(Context context, ServiceConnectionObserver connectionObserver) {
        /*Adding observer should be the first operation in this function*/
        mConnectionObservers.add(connectionObserver);

        //first element binding to service
        if (mConnectionObservers.size() == 1) {
            //TODO: maybe remove all commands from onStartCommand()
            context.startService(new Intent(context, MediaPlaybackService.class));
        }

        //service hasn't been connected yet
        if (mService == null) {
            /*Better to use application context, because
            * after recreating activity on configuration changes
            * activity context which was bind to service doesn't exists*/
            context = context.getApplicationContext();
            Intent service = new Intent(context, MediaPlaybackService.class);
            context.bindService(service, this, ContextWrapper.BIND_AUTO_CREATE);
        }
    }

    public void unbindFromService(Context context, ServiceConnectionObserver connectionObserver) {
        ServiceConnectionObserver candidateForUnbinding = connectionObserver;
        mConnectionObservers.remove(candidateForUnbinding);

        if (mConnectionObservers.size() == 0) {
            context = context.getApplicationContext();
            context.unbindService(this);
            mService = null;
        }
    }

    public void playAll(Cursor cursor, int position, String trackIdColumn) {
        if (mService != null) {
            long[] playlist = getPlaylistFromCursor(cursor, trackIdColumn);
            playAll(playlist, position);
        }
    }

    private long[] getPlaylistFromCursor(Cursor cursor, String trackIdColumn) {
        int cursorLinesQuantity = cursor.getCount();
        long[] playlist = new long[cursorLinesQuantity];
        cursor.moveToFirst();

        int idColumnIndex = cursor.getColumnIndexOrThrow(trackIdColumn);

        for (int i = 0; i < cursorLinesQuantity; i++) {
            playlist[i] = cursor.getLong(idColumnIndex);
            cursor.moveToNext();
        }
        return playlist;
    }

    public void playAll(long[] playlist, int position) {
        try {
            mService.open(playlist, position);
            mService.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void prev() {
        if (mService != null) {
            try {
                mService.prev();
            } catch (RemoteException e) {
            }
        }
    }

    public void seek(long position) {
        if (mService != null) {
            try {
                mService.seek(position);
            } catch (RemoteException e) {
            }
        }
    }

    public void next() {
        if (mService != null) {
            try {
                mService.next();
            } catch (RemoteException e) {
            }
        }
    }

    public void play() {
        if (mService != null) {
            try {
                mService.play();
            } catch (RemoteException e) {
            }
        }
    }

    public void pause() {
        if (mService != null) {
            try {
                mService.pause();
            } catch (RemoteException e) {
            }
        }
    }


    public void removeTrack(long trackId) {
        if (mService != null) {
            try {
                mService.removeTrack(trackId);
            } catch (RemoteException e) {
            }
        }
    }

    public void addToTheEndOfPlayQueue(long[] list) {
        if (mService != null) {
            try {
                mService.enqueue(list, MediaPlaybackService.LAST);
            } catch (RemoteException e) {
            }
        }
    }

    public void removeFromPlayQueue(long[] list) {
        if (mService != null) {
            try {
                mService.removeFromQueue(list);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPlaying() {
        if (mService != null) {
            try {
                return mService.isPlaying();
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public long getTrackDuration() {
        if (mService != null) {
            try {
                return mService.duration();
            } catch (RemoteException e) {
            }
        }
        return ERROR_RETURN_VALUE;
    }

    public long getPlayingPosition() {
        if (mService != null) {
            try {
                return mService.position();
            } catch (RemoteException e) {
            }
        }
        return ERROR_RETURN_VALUE;
    }

    public String getArtistName() {
        if (mService != null) {
            try {
                return mService.getArtistName();
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public String getTrackName() {
        if (mService != null) {
            try {
                return mService.getTrackName();
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public void setRepeatMode(int repeatMode) {
        if (mService != null) {
            try {
                mService.setRepeatMode(repeatMode);
            } catch (RemoteException e) {
            }
        }
    }

    public int getRepeatMode() {
        if (mService != null) {
            try {
                return mService.getRepeatMode();
            } catch (RemoteException e) {
            }
        }
        return ERROR_RETURN_VALUE;
    }

    public void setShuffleMode(int shuffleMode) {
        if (mService != null) {
            try {
                mService.setShuffleMode(shuffleMode);
            } catch (RemoteException e) {
            }
        }
    }

    public int getShuffleMode() {
        if (mService != null) {
            try {
                return mService.getShuffleMode();
            } catch (RemoteException e) {
            }
        }
        return ERROR_RETURN_VALUE;
    }

    public long getAlbumID() {
        if (mService != null) {
            try {
                return mService.getAlbumId();
            } catch (RemoteException e) {
            }
        }
        return AudioStorage.WRONG_ID;
    }

    public long getArtistID() {
        if (mService != null) {
            try {
                return mService.getArtistId();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return AudioStorage.WRONG_ID;
    }

    public long getTrackID() {
        if (mService != null) {
            try {
                return mService.getAudioId();
            } catch (RemoteException e) {
            }
        }
        return AudioStorage.WRONG_ID;
    }

    public void updateCurrentTrackInfo() {
        if (mService != null) {
            try {
                mService.updateCurrentTrackInfo();
            } catch (RemoteException e) {
            }
        }
    }

    public long[] getQueue() {
        if (mService != null) {
            try {
                return mService.getQueue();
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public void moveQueueItem(int from, int to) {
        if (mService != null) {
            try {
                mService.moveQueueItem(from, to);
            } catch (RemoteException e) {
            }
        }
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "ServiceConntected");
        mService = IMediaPlaybackService.Stub.asInterface(service);
        /* Notify all observers.
         * In most cases here will be notified
         * one observer, which is added to collection first.
         * But sometimes binding can delays and in this case
         * more than one observer can be notified */
        for (ServiceConnectionObserver observer : mConnectionObservers) {
            observer.ServiceConnected();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        for (ServiceConnectionObserver observer : mConnectionObservers) {
            observer.ServiceDisconnected();
        }
        mService = null;
    }
}
