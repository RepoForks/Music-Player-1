package ua.edu.cdu.fotius.lisun.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

import ua.edu.cdu.fotius.lisun.musicplayer.notification.MediaControlActionsReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.notification.MediaNotificationManager;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ExternalCard;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ListenedDetector;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MultiPlayer;
import ua.edu.cdu.fotius.lisun.musicplayer.service.PlaybackHistory;
import ua.edu.cdu.fotius.lisun.musicplayer.service.Playlist;

/**
 * Provides "background" audio playback capabilities, allowing the
 * user to switch between activities without stopping playback.
 */
public class MediaPlaybackService extends Service {

    /**
     * used to specify whether enqueue() should start playing
     * the new list of files right away, next or once all the currently
     * queued files have been played
     */


    public static final int NOW = 1;
    public static final int NEXT = 2;
    public static final int LAST = 3;

    //TODO:
    public static final String PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
    public static final String META_CHANGED = "com.android.music.metachanged";
    public static final String QUEUE_CHANGED = "com.android.music.queuechanged";

    public static final int TRACK_ENDED = 1;
    public static final int RELEASE_WAKELOCK = 2;
    public static final int SERVER_DIED = 3;
    private static final int FOCUSCHANGE = 4;
    private static final int FADEDOWN = 5;
    private static final int FADEUP = 6;
    public static final int TRACK_WENT_TO_NEXT = 7;
    public static final int ACQUIRE_WAKELOCK = 8;
    private static final int ACQUIRE_WAKELOCK_TIMEOUT = 30000;
    //
    private MultiPlayer mPlayer;
    private String mFileToPlay;
    private int mMediaMountedCount = 0;
    private long[] mAutoShuffleList = null;

    private PlaybackHistory mHistory;
    private Cursor mCursor;
    private Playlist mPlaylist;

    private static final String LOGTAG = "MediaPlaybackService";
    private final Shuffler mRand = new Shuffler();
    private int mOpenFailedCounter = 0;
    String[] mCursorCols = new String[]{
            "audio._id AS _id",             // index must match IDCOLIDX below
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.IS_PODCAST, // index must match PODCASTCOLIDX below
            MediaStore.Audio.Media.BOOKMARK    // index must match BOOKMARKCOLIDX below
    };
    private final static int IDCOLIDX = 0;
    private final static int PODCASTCOLIDX = 8;
    private final static int BOOKMARKCOLIDX = 9;
    private BroadcastReceiver mUnmountReceiver = null;
    private WakeLock mWakeLock;
    private int mServiceStartId = -1;
    private boolean mServiceInUse = false;
    private boolean mIsSupposedToBePlaying = false;
    private AudioManager mAudioManager;

    private boolean mQuietMode = false;
    private boolean mQueueIsSaveable = true;
    private SharedPreferences mPreferences;
    //TODO:Move this and from MusicUtils to new class ExternalStorage...
    //private int mCardId;
    private ExternalCard mCardId;
    // used to track what type of audio focus loss caused the playback to pause
    private boolean mPausedByTransientLossOfFocus = false;


    // interval after which we stop the service when idle
    private static final int IDLE_DELAY = 1000;

    private MediaSessionCompat mSession;
    private MediaControllerCompat mController;
    private MediaControllerCompat.TransportControls mTransportControls;
    private MediaNotificationManager mMediaNotificationManager;

    private ListenedDetector mListenedDetector;

    //Also used by MultiPlayer
    private Handler mMediaplayerHandler = new Handler() {
        float mCurrentVolume = 1.0f;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADEDOWN:
                    mCurrentVolume -= .05f;
                    if (mCurrentVolume > .2f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEDOWN, 10);
                    } else {
                        mCurrentVolume = .2f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case FADEUP:
                    mCurrentVolume += .01f;
                    if (mCurrentVolume < 1.0f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEUP, 10);
                    } else {
                        mCurrentVolume = 1.0f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case SERVER_DIED:
                    if (mIsSupposedToBePlaying) {
                        gotoNext(true);
                    } else {
                        // the server died when we were idle, so just
                        // reopen the same song (it will start again
                        // from the beginning though when the user
                        // restarts)
                        openCurrentAndNext();
                    }
                    break;
                case TRACK_WENT_TO_NEXT:
                    //TODO: favorite
                    mListenedDetector.onPlaybackEnded(ListenedDetector.DEFAULT_VALUE);

                    mPlaylist.setPlayPosition(mPlaylist.getNextPlayPosition());
                    if (mCursor != null) {
                        mCursor.close();
                        mCursor = null;
                    }
                    if ((mPlaylist.getPlayPosition() >= 0) && (mPlaylist.getPlayPosition() < mPlaylist.size())) {
                        mCursor = getCursorForId(mPlaylist.getCurrentTrack());
                    }
                    //update GUI
                    notifyChange(META_CHANGED);
                    //update Notification in dropdown
                    //bar
                    //TODO: update notification
                    mMediaNotificationManager.startOrUpdateNotification(composeMetadata(), getAlbumId(), isPlaying());

                    //updateNotification();
                    //move to next track
                    setNextTrack();

                    //TODO: favorite
                    mListenedDetector.onPlaybackStarted(getAudioId(), duration(), position());

                    break;
                case TRACK_ENDED:
                    Log.d(TAG, "Track ended");

                    //TODO: favorite
                    mListenedDetector.onPlaybackEnded(ListenedDetector.DEFAULT_VALUE);

                    if (mPlaylist.getRepeatMode() == Playlist.REPEAT_CURRENT) {
                        seek(0);
                        play();
                    } else {
                        gotoNext(false);
                    }
                    break;

                case ACQUIRE_WAKELOCK:
                    mWakeLock.acquire(ACQUIRE_WAKELOCK_TIMEOUT);
                    break;

                case RELEASE_WAKELOCK:
                    mWakeLock.release();
                    break;

                case FOCUSCHANGE:
                    // This code is here so we can better synchronize it with the code that
                    // handles fade-in
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            if (isPlaying()) {
                                mPausedByTransientLossOfFocus = false;
                            }
                            pause();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            mMediaplayerHandler.removeMessages(FADEUP);
                            mMediaplayerHandler.sendEmptyMessage(FADEDOWN);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            if (isPlaying()) {
                                mPausedByTransientLossOfFocus = true;
                            }
                            pause();
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (!isPlaying() && mPausedByTransientLossOfFocus) {
                                mPausedByTransientLossOfFocus = false;
                                mCurrentVolume = 0f;
                                mPlayer.setVolume(mCurrentVolume);
                                play(); // also queues a fade-in
                            } else {
                                mMediaplayerHandler.removeMessages(FADEDOWN);
                                mMediaplayerHandler.sendEmptyMessage(FADEUP);
                            }
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    };

    //TODO:
    private final String TAG = getClass().getSimpleName();

    private BroadcastReceiver mHeadSetPlug = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        if(isPlaying()) {
                            pause();
                        }
                        break;
                    case 1: //PLUGGED
                        break;
                }
            }
        }
    };

    private OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener() {
        /**
         * @param focusChange  AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_LOSS
         */
        public void onAudioFocusChange(int focusChange) {
            //obtainMessage returns Message.target = this
            //so can call sendToTarget()
            mMediaplayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };

    private MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            pause();
        }

        @Override
        public void onPlay() {
            play();
        }

        @Override
        public void onSkipToNext() {
            gotoNext(true);
        }

        @Override
        public void onSkipToPrevious() {
            prev();
        }
    };

    public MediaPlaybackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPlaylist = Playlist.getInstance();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mPreferences = getSharedPreferences("Music", MODE_PRIVATE);
        mHistory = PlaybackHistory.getInstance();

        mCardId = new ExternalCard(this);
        //Register listener which listens if if storage mounted or
        //ejected
        registerExternalStorageListener();

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mHeadSetPlug, filter);

        mListenedDetector = new ListenedDetector(this);
        // Needs to be done in this thread, since otherwise ApplicationContext.getPowerManager() crashes.
        mPlayer = new MultiPlayer(this);
        mPlayer.setHandler(mMediaplayerHandler);

        reloadQueue();
        //Calls saveQueue(true)
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);

        ComponentName actionsReceiver = new ComponentName(getPackageName(),
                MediaControlActionsReceiver.class.getName());
        mSession = new MediaSessionCompat(this, "MusicService", actionsReceiver, null);
        mSession.setCallback(mMediaSessionCallback);

        try {
            mController = new MediaControllerCompat(this, mSession.getSessionToken());
            mTransportControls = mController.getTransportControls();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mMediaNotificationManager = new MediaNotificationManager(this, mTransportControls);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.setReferenceCounted(false);

        // If the service was idle, but got killed before it stopped itself, the
        // system will relaunch it. Make sure it gets stopped again in that case.
        Message msg = mDelayedStopHandler.obtainMessage();
        mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
    }

    private MediaMetadataCompat composeMetadata() {
        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, getTrackName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getAlbumName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getArtistName());
        return metadataBuilder.build();
    }

    @Override
    public void onDestroy() {
        // Check that we're not being destroyed while something is still playing.
        if (isPlaying()) {
            Log.e(LOGTAG, "Service being destroyed while still playing.");
        }

        // release all MediaPlayer resources, including the native player and wakelocks
        Intent i = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(i);
        mPlayer.release();
        mPlayer = null;

        mAudioManager.abandonAudioFocus(mAudioFocusListener);

        // make sure there aren't any other messages coming
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mMediaplayerHandler.removeCallbacksAndMessages(null);

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        //unregisterReceiver(mIntentReceiver);
        unregisterReceiver(mHeadSetPlug);

        if (mUnmountReceiver != null) {
            unregisterReceiver(mUnmountReceiver);
            mUnmountReceiver = null;
        }
        mWakeLock.release();

        super.onDestroy();
    }

    public void setPausedByTransientLossOfFocus(boolean paused) {
        mPausedByTransientLossOfFocus = paused;
    }

    private void saveQueue(boolean full) {
        if (!mQueueIsSaveable) {
            return;
        }

        Editor ed = mPreferences.edit();
        if (full) {

            ed.putString("queue", mPlaylist.toString());
            ed.putInt("cardid", mCardId.get());

            if (mPlaylist.getShuffleMode() != Playlist.SHUFFLE_NONE) {
                // In shuffle mode we need to save the history too
                ed.putString("history", mHistory.toString());
            }
        }
        ed.putInt("curpos", mPlaylist.getPlayPosition());

        if (mPlayer.isInitialized()) {
            ed.putLong("seekpos", mPlayer.position());
        }
        ed.putInt("repeatmode", mPlaylist.getRepeatMode());
        ed.putInt("shufflemode", mPlaylist.getShuffleMode());
        //made this async
        ed.apply();
    }

    /**
     * Reads queue from preferences file as string
     * Parses it to current playlist(mPlaylist)
     */
    private void reloadQueue() {
        String retreivedQueue = null;

        int id = mCardId.get();
        if (mPreferences.contains("cardid")) {
            id = mPreferences.getInt("cardid", ~(mCardId.get()));
        }
        if (id == mCardId.get()) {
            // Only restore the saved playlist if the card is still
            // the same one as when the playlist was saved
            retreivedQueue = mPreferences.getString("queue", "");
        }

        int qlen = (retreivedQueue != null) ? retreivedQueue.length() : 0;
        //if we've got some queue from preferences file
        //parse this queue
        //Queue scheme is: "_id;_id;_id..."
        if (qlen > 1) {

            mPlaylist.fromString(retreivedQueue);

            int pos = mPreferences.getInt("curpos", 0);
            if (pos < 0 || pos >= mPlaylist.size()) {
                // The saved playlist is bogus, discard it
                mPlaylist.clear();
                return;
            }
            mPlaylist.setPlayPosition(pos);

            // Make sure we don't auto-skip to the next song, since that
            // also starts playback. What could happen in that case is:
            // - music is paused
            // - go to UMS and delete some files, including the currently playing one
            // - come back from UMS
            // (time passes)
            // - music app is killed for some reason (out of memory)
            // - music service is restarted, service restores state, doesn't find
            //   the "current" file, goes to the next and: playback starts on its
            //   own, potentially at some random inconvenient time.
            mOpenFailedCounter = 20;
            mQuietMode = true;
            openCurrentAndNext();
            mQuietMode = false;
            if (!mPlayer.isInitialized()) {
                // couldn't restore the saved state
                mPlaylist.clear();
                return;
            }

            long seekpos = mPreferences.getLong("seekpos", 0);
            seek(seekpos >= 0 && seekpos < duration() ? seekpos : 0);

            int repmode = mPreferences.getInt("repeatmode", Playlist.REPEAT_NONE);
            if (repmode != Playlist.REPEAT_ALL && repmode != Playlist.REPEAT_CURRENT) {
                repmode = Playlist.REPEAT_NONE;
            }
            mPlaylist.setRepeatMode(repmode);

            int shufmode = mPreferences.getInt("shufflemode", Playlist.SHUFFLE_NONE);
            if (shufmode != Playlist.SHUFFLE_AUTO && shufmode != Playlist.SHUFFLE_NORMAL) {
                shufmode = Playlist.SHUFFLE_NONE;
            }
            if (shufmode != Playlist.SHUFFLE_NONE) {
                // in shuffle mode we need to restore the history too
                retreivedQueue = mPreferences.getString("history", "");
                qlen = retreivedQueue != null ? retreivedQueue.length() : 0;
                if (qlen > 1) {
                    mHistory.fromString(retreivedQueue, mPlaylist.size());
                }
            }
            if (shufmode == Playlist.SHUFFLE_AUTO) {
                if (!makeAutoShuffleList()) {
                    shufmode = Playlist.SHUFFLE_NONE;
                }
            }
            mPlaylist.setShuffleMode(shufmode);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mServiceInUse = true;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mServiceInUse = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceStartId = startId;
        mDelayedStopHandler.removeCallbacksAndMessages(null);

        // make sure the service will shut down on its own if it was
        // just started but not bound to and nothing is playing
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        Message msg = mDelayedStopHandler.obtainMessage();
        mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInUse = false;

        // Take a snapshot of the current playlist
        saveQueue(true);

        if (isPlaying() || mPausedByTransientLossOfFocus) {
            // something is currently playing, or will be playing once
            // an in-progress action requesting audio focus ends, so don't stop the service now.
            return true;
        }

        // If there is a playlist but playback is paused, then wait a while
        // before stopping the service, so that pause/resume isn't slow.
        // Also delay stopping the service if we're transitioning between tracks.
        if (mPlaylist.size() > 0 || mMediaplayerHandler.hasMessages(TRACK_ENDED)) {
            Message msg = mDelayedStopHandler.obtainMessage();
            mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
            return true;
        }

        // No active playlist, OK to stop the service right now
        stopSelf(mServiceStartId);
        return true;
    }

    private Handler mDelayedStopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Check again to make sure nothing is playing right now
            if (isPlaying() || mPausedByTransientLossOfFocus || mServiceInUse
                    || mMediaplayerHandler.hasMessages(TRACK_ENDED)) {
                return;
            }
            // save the queue again, because it might have changed
            // since the user exited the music app (because of
            // party-shuffle or because the play-position changed)
            saveQueue(true);
            stopSelf(mServiceStartId);
        }
    };

    /**
     * Called when we receive a ACTION_MEDIA_EJECT notification.
     *
     * @param storagePath path to mount point for the removed media
     */
    public void closeExternalStorageFiles(String storagePath) {
        // stop playback and clean up if the SD card is going to be unmounted.
        stop(true);
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);
    }

    /**
     * Registers an intent to listen for ACTION_MEDIA_EJECT notifications.
     * The intent will call closeExternalStorageFiles() if the external media
     * is going to be ejected, so applications can clean up any files they have open.
     */
    private void registerExternalStorageListener() {
        if (mUnmountReceiver == null) {
            mUnmountReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                        saveQueue(true);
                        mQueueIsSaveable = false;
                        closeExternalStorageFiles(intent.getData().getPath());
                    } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                        mMediaMountedCount++;
                        mCardId = new ExternalCard(MediaPlaybackService.this);
                        reloadQueue();
                        mQueueIsSaveable = true;
                        notifyChange(QUEUE_CHANGED);
                        notifyChange(META_CHANGED);
                    }
                }
            };
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            iFilter.addDataScheme("file");
            registerReceiver(mUnmountReceiver, iFilter);
        }
    }

    /**
     * Notify the change-receivers that something has changed.
     * The intent that is sent contains the following data
     * for the currently playing track:
     * "id" - Integer: the database row ID
     * "artist" - String: the name of the artist
     * "album" - String: the name of the album
     * "track" - String: the name of the track
     * The intent has an action that is one of
     * "com.android.music.metachanged"
     * "com.android.music.queuechanged",
     * "com.android.music.playbackcomplete"
     * "com.android.music.playstatechanged"
     * respectively indicating that a new track has
     * started playing, that the playback queue has
     * changed, that playback has stopped because
     * the last file in the list has been played,
     * or that the play-state changed (paused/resumed).
     *
     * @param what is some action(example, MediaPlaybackService.QUEUE_CHANGED)
     */
    private void notifyChange(String what) {
        Intent i = new Intent(what);
        i.putExtra("id", Long.valueOf(getAudioId()));
        i.putExtra("artist", getArtistName());
        i.putExtra("album", getAlbumName());
        i.putExtra("track", getTrackName());
        i.putExtra("playing", isPlaying());
        sendBroadcast(i);

        if (what.equals(QUEUE_CHANGED)) {
            saveQueue(true);
        } else {
            saveQueue(false);
        }
    }

    private void closeCursorAndChangeMetadataOnEmptyPlaylist() {
        if (mPlaylist.size() == 0) {
            mCursor.close();
            mCursor = null;
            notifyChange(META_CHANGED);
        }
    }

    /**
     * Appends a list of tracks to the current playlist.
     * If nothing is playing currently, playback will be started at
     * the first track.
     * If the action is NOW, playback will switch to the first of
     * the new tracks immediately.
     *
     * @param list   The list of tracks to append.
     * @param action NOW, NEXT or LAST
     */
    public void enqueue(long[] list, int action) {
        synchronized (this) {
            if (action == NEXT && mPlaylist.getPlayPosition() + 1 < mPlaylist.size()) {
                //addToPlayList(list, mPlaylist.getPlayPosition() + 1);
                mPlaylist.addAllTracks(mPlaylist.getPlayPosition() + 1, list);
                closeCursorAndChangeMetadataOnEmptyPlaylist();
                notifyChange(QUEUE_CHANGED);
            } else {
                // action == LAST || action == NOW || mPlayPos + 1 == mPlayListLen
                //addToPlayList(list, Integer.MAX_VALUE);
                mPlaylist.addAllTracks(Integer.MAX_VALUE, list);
                closeCursorAndChangeMetadataOnEmptyPlaylist();
                notifyChange(QUEUE_CHANGED);

                if (action == NOW) {
                    //mPlayPos = mPlaylist.size() - list.length;
                    mPlaylist.setPlayPosition(mPlaylist.size() - list.length);
                    openCurrentAndNext();
                    play();
                    notifyChange(META_CHANGED);
                    return;
                }
            }
            if (mPlaylist.getPlayPosition() < 0) {
                //mPlayPos = 0;
                mPlaylist.setPlayPosition(0);
                openCurrentAndNext();
                play();
                notifyChange(META_CHANGED);
            }
        }
    }

    /**
     * Replaces the current playlist with a new list,
     * and prepares for starting playback at the specified
     * position in the list, or a random position if the
     * specified position is 0.
     *
     * @param list The new list of tracks.
     */
    public void open(long[] list, int position) {
        synchronized (this) {
            if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_AUTO) {
                //mShuffleMode = Playlist.SHUFFLE_NORMAL;
                mPlaylist.setShuffleMode(Playlist.SHUFFLE_NORMAL);
            }
            long oldId = getAudioId();
            int listlength = list.length;
            boolean newlist = true;
            if (mPlaylist.size() == listlength) {
                // possible fast path: list might be the same
                newlist = false;
                for (int i = 0; i < listlength; i++) {
                    //if (list[i] != mPlayList[i]) {
                    if (list[i] != mPlaylist.getTrack(i)) {
                        newlist = true;
                        break;
                    }
                }
            }

            if (position >= 0) {
                mPlaylist.setPlayPosition(position);
            } else {
                mPlaylist.setPlayPosition(mRand.nextInt(mPlaylist.size()));
            }

            if (newlist) {
                mPlaylist.addAllTracks(-1, list);
                closeCursorAndChangeMetadataOnEmptyPlaylist();
                notifyChange(QUEUE_CHANGED);
            }
            mHistory.clear();

            saveBookmarkIfNeeded();

            openCurrentAndNext();
            if (oldId != getAudioId()) {
                notifyChange(META_CHANGED);
            }
        }
    }

    /**
     * Moves the item at index1 to index2.
     *
     * @param index1
     * @param index2
     */
    public void moveQueueItem(int index1, int index2) {
        synchronized (this) {
            if (index1 >= mPlaylist.size()) {
                index1 = mPlaylist.size() - 1;
            }
            if (index2 >= mPlaylist.size()) {
                index2 = mPlaylist.size() - 1;
            }

            long tmp = mPlaylist.getTrack(index1);
            mPlaylist.removeTrack(index1);
            mPlaylist.addTrackId(index2, tmp);

            if (mPlaylist.getPlayPosition() == index1) {
                mPlaylist.setPlayPosition(index2);
            } else if (mPlaylist.getPlayPosition() >= index1 && mPlaylist.getPlayPosition() <= index2) {
                mPlaylist.setPlayPosition(mPlaylist.getPlayPosition() - 1);
            } else if (mPlaylist.getPlayPosition() >= index2 && mPlaylist.getPlayPosition() <= index1) {
                mPlaylist.setPlayPosition(mPlaylist.getPlayPosition() + 1);
            }
            notifyChange(QUEUE_CHANGED);
        }
    }

    /**
     * Returns the current play list
     *
     * @return An array of integers containing the IDs of the tracks in the play list
     */
    public long[] getQueue() {
        synchronized (this) {
            int len = mPlaylist.size();
            long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                //list[i] = mPlayList[i];
                list[i] = mPlaylist.getTrack(i);
            }
            return list;
        }
    }

    private Cursor getCursorForId(long lid) {
        String id = String.valueOf(lid);

        Cursor c = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mCursorCols, "_id=" + id, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /**
     * Trying set data source of mPlayList[mPlayPos] to mCurrentMediaPlayer
     */
    private void openCurrentAndNext() {
        synchronized (this) {
            //clear cursor
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }

            //if nothing to play
            if (mPlaylist.size() == 0) {
                return;
            }
            stop(false);

            //retreive song information from database by song _ID
            mCursor = getCursorForId(mPlaylist.getCurrentTrack());

            //trying to open song
            //if can't try next, if can't try next and so on 10 times or
            //until playlist ends
            while (true) {
                if (mCursor != null && mCursor.getCount() != 0 &&
                        open(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" +
                                mCursor.getLong(IDCOLIDX))) {
                    break;
                }
                // if we get here then opening the file failed. We can close the cursor now, because
                // we're either going to create a new one next, or stop trying
                if (mCursor != null) {
                    mCursor.close();
                    mCursor = null;
                }
                if (mOpenFailedCounter++ < 10 && mPlaylist.size() > 1) {

                    int pos = getNextPosition(false);
                    if (pos < 0) {
                        goToIdleAndNotify();
                        return;
                    }


                    mPlaylist.setPlayPosition(pos);
                    stop(false);
                    //mPlayPos = pos;
                    mPlaylist.setPlayPosition(pos);
                    mCursor = getCursorForId(mPlaylist.getCurrentTrack());
                } else {
                    mOpenFailedCounter = 0;
                    if (!mQuietMode) {
                        Toast.makeText(this, R.string.playback_failed, Toast.LENGTH_SHORT).show();
                    }
                    goToIdleAndNotify();
                    return;
                }
            }

            // go to bookmark if needed
            if (isPodcast()) {
                long bookmark = getBookmark();
                // Start playing a little bit before the bookmark,
                // so it's easier to get back in to the narrative.
                seek(bookmark - 5000);
            }
            setNextTrack();
        }
    }

    private void goToIdleAndNotify() {
        gotoIdleState();
        if (mIsSupposedToBePlaying) {
            mIsSupposedToBePlaying = false;
            notifyChange(PLAYSTATE_CHANGED);
        }
    }

    private void setNextTrack() {
        mPlaylist.setNextPlayPosition(getNextPosition(false));

        if (mPlaylist.getNextPlayPosition() >= 0) {
            long id = mPlaylist.getTrack(mPlaylist.getNextPlayPosition());
            //Set current player(mCurrentMediaPlayer) with new data source or if failes
            mPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + id);
        } else {
            mPlayer.setNextDataSource(null);
        }
    }

    /**
     * Opens the specified file and readies it for playback.
     *
     * @param path The full path of the file to be opened.
     */
    public boolean open(String path) {
        synchronized (this) {
            if (path == null) {
                return false;
            }

            // if mCursor is null, try to associate path with a database cursor
            if (mCursor == null) {

                ContentResolver resolver = getContentResolver();
                Uri uri;
                String where;
                String selectionArgs[];
                if (path.startsWith("content://media/")) {
                    uri = Uri.parse(path);
                    where = null;
                    selectionArgs = null;
                } else {
                    uri = MediaStore.Audio.Media.getContentUriForPath(path);
                    where = MediaStore.Audio.Media.DATA + "=?";
                    selectionArgs = new String[]{path};
                }

                try {
                    mCursor = resolver.query(uri, mCursorCols, where, selectionArgs, null);
                    if (mCursor != null) {
                        if (mCursor.getCount() == 0) {
                            mCursor.close();
                            mCursor = null;
                        } else {
                            mCursor.moveToNext();
                            //ensurePlayListCapacity(1);
                            //mPlayListLen = 1;
                            //mPlayList[0] = mCursor.getLong(IDCOLIDX);
                            //mPlayPos = 0;
                            mPlaylist.clear();
                            mPlaylist.addTrack(mCursor.getLong(IDCOLIDX));
                            mPlaylist.setPlayPosition(0);
                        }
                    }
                } catch (UnsupportedOperationException ex) {
                }
            }
            mFileToPlay = path;
            mPlayer.setDataSource(mFileToPlay);
            if (mPlayer.isInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }
            stop(true);
            return false;
        }
    }

    /**
     * Starts playback of a previously opened file.
     */
    public void play() {
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (mPlayer.isInitialized()) {
            // if we are at the end of the song, go to the next song first
            long duration = mPlayer.duration();
            if (mPlaylist.getRepeatMode() != Playlist.REPEAT_CURRENT && duration > 2000 &&
                    mPlayer.position() >= duration - 2000) {
                gotoNext(true);
            }

            mPlayer.start();
            mListenedDetector.onPlaybackStarted(getAudioId(), duration(), position());
            // make sure we fade in, in case a previous fadein was stopped because
            // of another focus loss
            mMediaplayerHandler.removeMessages(FADEDOWN);
            mMediaplayerHandler.sendEmptyMessage(FADEUP);

            if (!mIsSupposedToBePlaying) {
                mIsSupposedToBePlaying = true;
                notifyChange(PLAYSTATE_CHANGED);
            }

            mMediaNotificationManager.startOrUpdateNotification(composeMetadata(), getAlbumId(), isPlaying());

        } else if (mPlaylist.size() <= 0) {
            setShuffleMode(Playlist.SHUFFLE_AUTO);
        }
    }

    private void stop(boolean remove_status_icon) {
        if (mPlayer != null && mPlayer.isInitialized()) {
            mListenedDetector.onPlaybackEnded(position());
            mPlayer.stop();
        }
        mFileToPlay = null;
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        if (remove_status_icon) {
            gotoIdleState();
        }

        if (remove_status_icon) {
            mIsSupposedToBePlaying = false;
        }
    }

    /**
     * Stops playback.
     */
    public void stop() {
        stop(true);
    }

    /**
     * Pauses playback (call play() to resume)
     */
    public void pause() {
        synchronized (this) {
            mMediaplayerHandler.removeMessages(FADEUP);
            if (isPlaying()) {
                mListenedDetector.onPlaybackEnded(position());
                //spoil values, so if stop(...) calls mFavoriteSongDetector.onPlaybackEnded(...)
                //song will not be counted 2 times
                mListenedDetector.onPlaybackStarted(ListenedDetector.DEFAULT_VALUE,
                        ListenedDetector.DEFAULT_VALUE, ListenedDetector.DEFAULT_VALUE);

                mPlayer.pause();
                gotoIdleState();
                mIsSupposedToBePlaying = false;
                notifyChange(PLAYSTATE_CHANGED);
                saveBookmarkIfNeeded();
            }
        }
    }

    /**
     * Returns whether something is currently playing
     *
     * @return true if something is playing (or will be playing shortly, in case
     * we're currently transitioning between tracks), false if not.
     */
    public boolean isPlaying() {
        return mIsSupposedToBePlaying;
    }

    /*
      Desired behavior for prev/next/shuffle:

      - NEXT will move to the next track in the list when not shuffling, and to
        a track randomly picked from the not-yet-played tracks when shuffling.
        If all tracks have already been played, pick from the full set, but
        avoid picking the previously played track if possible.
      - when shuffling, PREV will go to the previously played track. Hitting PREV
        again will go to the track played before that, etc. When the start of the
        history has been reached, PREV is a no-op.
        When not shuffling, PREV will go to the sequentially previous track (the
        difference with the shuffle-case is mainly that when not shuffling, the
        user can back up to tracks that are not in the history).

        Example:
        When playing an album with 10 tracks from the start, and enabling shuffle
        while playing track 5, the remaining tracks (6-10) will be shuffled, e.g.
        the final play order might be 1-2-3-4-5-8-10-6-9-7.
        When hitting 'prev' 8 times while playing track 7 in this example, the
        user will go to tracks 9-6-10-8-5-4-3-2. If the user then hits 'next',
        a random track will be picked again. If at any time user disables shuffling
        the next/previous track will be picked in sequential order again.
     */
    public void prev() {
        synchronized (this) {
            if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_NORMAL) {
                // go to previously-played track and remove it from the history
                int histsize = mHistory.size();
                if (histsize == 0) {
                    // prev is a no-op
                    return;
                }
                int pos = mHistory.remove(histsize - 1);
                mPlaylist.setPlayPosition(pos);
            } else {
                if (mPlaylist.getPlayPosition() > 0) {
                    mPlaylist.setPlayPosition(mPlaylist.getPlayPosition() - 1);
                } else {
                    mPlaylist.setPlayPosition(mPlaylist.size() - 1);
                }
            }
            saveBookmarkIfNeeded();
            stop(false);
            openCurrentAndNext();
            play();
            notifyChange(META_CHANGED);
        }
    }

    /**
     * Get the next position to play. Note that this may actually modify mPlayPos
     * if playback is in SHUFFLE_AUTO mode and the shuffle list window needed to
     * be adjusted. Either way, the return value is the next value that should be
     * assigned to mPlayPos;
     */
    private int getNextPosition(boolean force) {
        if (mPlaylist.getRepeatMode() == Playlist.REPEAT_CURRENT) {
            if (mPlaylist.getPlayPosition() < 0) return 0;
            //return mPlayPos;
            return mPlaylist.getPlayPosition();
        } else if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_NORMAL) {
            // Pick random next track from the not-yet-played ones
            // TODO: make it work right after adding/removing items in the queue.

            // Store the current file in the history, but keep the history at a
            // reasonable size
            if (mPlaylist.getPlayPosition() >= 0) {
                mHistory.add(mPlaylist.getPlayPosition());
            }
            if (mHistory.size() > PlaybackHistory.MAX_HISTORY_SIZE) {
                mHistory.remove(0);
            }

            int numTracks = mPlaylist.size();
            int[] tracks = new int[numTracks];
            for (int i = 0; i < numTracks; i++) {
                tracks[i] = i;
            }

            int numHistory = mHistory.size();
            int numUnplayed = numTracks;
            for (int i = 0; i < numHistory; i++) {
                int idx = mHistory.get(i);
                if (idx < numTracks && tracks[idx] >= 0) {
                    numUnplayed--;
                    tracks[idx] = -1;
                }
            }

            // 'numUnplayed' now indicates how many tracks have not yet
            // been played, and 'tracks' contains the indices of those
            // tracks.
            if (numUnplayed <= 0) {
                // everything's already been played
                if (mPlaylist.getRepeatMode() == Playlist.REPEAT_ALL || force) {
                    //pick from full set
                    numUnplayed = numTracks;
                    for (int i = 0; i < numTracks; i++) {
                        tracks[i] = i;
                    }
                } else {
                    // all done
                    return -1;
                }
            }
            int skip = mRand.nextInt(numUnplayed);
            int cnt = -1;
            while (true) {
                while (tracks[++cnt] < 0)
                    ;
                skip--;
                if (skip < 0) {
                    break;
                }
            }
            return cnt;
        } else if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_AUTO) {
            doAutoShuffleUpdate();
            return mPlaylist.getPlayPosition() + 1;
        } else {
            if (mPlaylist.getPlayPosition() >= mPlaylist.size() - 1) {

                // we're at the end of the list
                if (mPlaylist.getRepeatMode() == Playlist.REPEAT_NONE && !force) {
                    // all done
                    return -1;
                } else if (mPlaylist.getRepeatMode() == Playlist.REPEAT_ALL || force) {
                    return 0;
                }
                return -1;
            } else {
                return mPlaylist.getPlayPosition() + 1;
            }
        }
    }


    /**
     * Goes to next song in playlist
     *
     * @param force
     */
    public void gotoNext(boolean force) {
        synchronized (this) {
            if (mPlaylist.size() <= 0) {
                Log.d(LOGTAG, "No play queue");
                return;
            }

            //if there is nothing to play
            //stop playback and go to idle
            //state
            int pos = getNextPosition(force);
            if (pos < 0) {
                goToIdleAndNotify();
                return;
            }

            //mPlayPos = pos;
            mPlaylist.setPlayPosition(pos);
            saveBookmarkIfNeeded();
            //stop current playing
            stop(false);
            //mPlayPos = pos;
            mPlaylist.setPlayPosition(pos);
            //trying set data source of mPlayList[mPlayPos] to mCurrentMediaPlayer
            openCurrentAndNext();
            //play setted song
            play();
            notifyChange(META_CHANGED);
        }
    }

    private void gotoIdleState() {
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        Message msg = mDelayedStopHandler.obtainMessage();
        mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
        if(mMediaNotificationManager != null) {
            mMediaNotificationManager.stopNotification();
        }
    }

    private void saveBookmarkIfNeeded() {
        try {
            if (isPodcast()) {
                long pos = position();
                long bookmark = getBookmark();
                long duration = duration();
                if ((pos < bookmark && (pos + 10000) > bookmark) ||
                        (pos > bookmark && (pos - 10000) < bookmark)) {
                    // The existing bookmark is close to the current
                    // position, so don't update it.
                    return;
                }
                if (pos < 15000 || (pos + 10000) > duration) {
                    // if we're near the start or end, clear the bookmark
                    pos = 0;
                }

                // write 'pos' to the bookmark field
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.BOOKMARK, pos);
                Uri uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mCursor.getLong(IDCOLIDX));
                getContentResolver().update(uri, values, null, null);
            }
        } catch (SQLiteException ex) {
        }
    }

    // Make sure there are at least 5 items after the currently playing item
    // and no more than 10 items before.
    private void doAutoShuffleUpdate() {
        boolean notify = false;

        // remove old entries
        if (mPlaylist.getPlayPosition() > 10) {
            removeTracks(0, mPlaylist.getPlayPosition() - 9);
            notify = true;
        }
        // add new entries if needed
        int to_add = 7 - (mPlaylist.size() - (mPlaylist.getPlayPosition() < 0 ? -1 : mPlaylist.getPlayPosition()));
        for (int i = 0; i < to_add; i++) {
            // pick something at random from the list

            int lookback = mHistory.size();
            int idx = -1;
            while (true) {
                idx = mRand.nextInt(mAutoShuffleList.length);
                if (!wasRecentlyUsed(idx, lookback)) {
                    break;
                }
                lookback /= 2;
            }
            mHistory.add(idx);
            if (mHistory.size() > PlaybackHistory.MAX_HISTORY_SIZE) {
                mHistory.remove(0);
            }
            //ensurePlayListCapacity(mPlaylist.size() + 1);
            //mPlayList[mPlayListLen++] = mAutoShuffleList[idx];
            mPlaylist.addTrack(mAutoShuffleList[idx]);
            notify = true;
        }
        if (notify) {
            notifyChange(QUEUE_CHANGED);
        }
    }

    // check that the specified idx is not in the history (but only look at at
    // most lookbacksize entries in the history)
    private boolean wasRecentlyUsed(int idx, int lookbacksize) {

        // early exit to prevent infinite loops in case idx == mPlayPos
        if (lookbacksize == 0) {
            return false;
        }

        int histsize = mHistory.size();
        if (histsize < lookbacksize) {
            Log.d(LOGTAG, "lookback too big");
            lookbacksize = histsize;
        }
        int maxidx = histsize - 1;
        for (int i = 0; i < lookbacksize; i++) {
            long entry = mHistory.get(maxidx - i);
            if (entry == idx) {
                return true;
            }
        }
        return false;
    }

    // A simple variation of Random that makes sure that the
    // value it returns is not equal to the value it returned
    // previously, unless the interval is 1.
    private static class Shuffler {
        private int mPrevious;
        private Random mRandom = new Random();

        public int nextInt(int interval) {
            int ret;
            do {
                ret = mRandom.nextInt(interval);
            } while (ret == mPrevious && interval > 1);
            mPrevious = ret;
            return ret;
        }
    }

    ;

    private boolean makeAutoShuffleList() {
        ContentResolver res = getContentResolver();
        Cursor c = null;
        try {
            c = res.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media._ID}, MediaStore.Audio.Media.IS_MUSIC + "=1",
                    null, null);
            if (c == null || c.getCount() == 0) {
                return false;
            }
            int len = c.getCount();
            long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                c.moveToNext();
                list[i] = c.getLong(0);
            }
            mAutoShuffleList = list;
            return true;
        } catch (RuntimeException ex) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    /**
     * Removes the range of tracks specified from the play list. If a file within the range is
     * the file currently being played, playback will move to the next file after the
     * range.
     *
     * @param first The first file to be removed
     * @param last  The last file to be removed
     * @return the number of tracks deleted
     */
    public int removeTracks(int first, int last) {
        int numremoved = removeTracksInternal(first, last);
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }

    private int removeTracksInternal(int first, int last) {
        synchronized (this) {

            int removedQuantity = mPlaylist.removeTracksRange(first, last);
            if (removedQuantity == 0) {
                return 0;
            }

            boolean gotonext = false;
            if (first <= mPlaylist.getPlayPosition() && last >= mPlaylist.getPlayPosition()) {
                mPlaylist.setPlayPosition(first);
                gotonext = true;
            } else if (mPlaylist.getPlayPosition() > last) {
                mPlaylist.setPlayPosition(mPlaylist.getPlayPosition() - (last - first + 1));
            }

            if (gotonext) {
                if (mPlaylist.size() == 0) {
                    stop(true);
                    mPlaylist.setPlayPosition(-1);
                    if (mCursor != null) {
                        mCursor.close();
                        mCursor = null;
                    }
                } else {
                    if (mPlaylist.getPlayPosition() >= mPlaylist.size()) {
                        mPlaylist.setPlayPosition(0);
                    }
                    boolean wasPlaying = isPlaying();
                    stop(false);
                    openCurrentAndNext();
                    if (wasPlaying) {
                        play();
                    }
                }
                notifyChange(META_CHANGED);
            }
            return removedQuantity;
        }
    }

    /**
     * Removes all instances of the track with the given id
     * from the playlist.
     *
     * @param id The id to be removed
     * @return how many instances of the track were removed
     */
    public int removeTrack(long id) {

        Log.d("removeTrack", "in removeTrack");

        int numremoved = 0;
        synchronized (this) {
            for (int i = 0; i < mPlaylist.size(); i++) {
                //if (mPlayList[i] == id) {
                if (mPlaylist.getTrack(i) == id) {
                    numremoved += removeTracksInternal(i, i);
                    i--;
                }
            }
        }
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }

    public int removeTracks(long[] ids) {
        int numremoved = 0;
        synchronized (this) {
            for (int i = 0; i < ids.length; i++) {
                long id = ids[i];
                for (int j = 0; j < mPlaylist.size(); j++) {
                    //if (mPlayList[i] == id) {
                    if (mPlaylist.getTrack(j) == id) {
                        numremoved += removeTracksInternal(j, j);
                        j--;
                    }
                }
            }
        }
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }

    public void setShuffleMode(int shufflemode) {
        synchronized (this) {
            if (mPlaylist.getShuffleMode() == shufflemode && mPlaylist.size() > 0) {
                return;
            }

            //mShuffleMode = shufflemode;
            mPlaylist.setShuffleMode(shufflemode);

            if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_AUTO) {
                if (makeAutoShuffleList()) {
                    //mPlayListLen = 0;
                    mPlaylist.clear();

                    doAutoShuffleUpdate();

                    //mPlayPos = 0;
                    mPlaylist.setPlayPosition(0);

                    openCurrentAndNext();
                    play();
                    notifyChange(META_CHANGED);
                    return;
                } else {
                    // failed to createCursorLoader a list of files to shuffle
                    //mShuffleMode = Playlist.SHUFFLE_NONE;
                    mPlaylist.setShuffleMode(Playlist.SHUFFLE_NONE);
                }
            }
            saveQueue(false);
        }
    }

    public int getShuffleMode() {
        return mPlaylist.getShuffleMode();
    }

    public void setRepeatMode(int repeatmode) {
        synchronized (this) {
            mPlaylist.setRepeatMode(repeatmode);
            setNextTrack();
            saveQueue(false);
        }
    }

    public int getRepeatMode() {
        return mPlaylist.getRepeatMode();
    }

    public int getMediaMountedCount() {
        return mMediaMountedCount;
    }

    /**
     * Returns the path of the currently playing file, or null if
     * no file is currently playing.
     */
    public String getPath() {
        return mFileToPlay;
    }

    /**
     * Returns the rowid of the currently playing file, or -1 if
     * no file is currently playing.
     */
    public long getAudioId() {
        synchronized (this) {
            if (mPlaylist.getPlayPosition() >= 0 && mPlayer.isInitialized()) {
                //return mPlayList[mPlayPos];
                return mPlaylist.getCurrentTrack();
            }
        }
        return -1;
    }

    /**
     * Returns the position in the queue
     *
     * @return the position in the queue
     */
    public int getQueuePosition() {
        synchronized (this) {
            //return mPlayPos;
            return mPlaylist.getPlayPosition();
        }
    }

    /**
     * Starts playing the track at the given position in the queue.
     *
     * @param pos The position in the queue of the track that will be played.
     */
    public void setQueuePosition(int pos) {
        synchronized (this) {
            stop(false);
            mPlaylist.setPlayPosition(pos);
            openCurrentAndNext();
            play();
            notifyChange(META_CHANGED);
            if (mPlaylist.getShuffleMode() == Playlist.SHUFFLE_AUTO) {
                doAutoShuffleUpdate();
            }
        }
    }

    public String getArtistName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        }
    }

    public long getArtistId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
        }
    }

    public String getAlbumName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        }
    }

    public long getAlbumId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        }
    }

    public String getTrackName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        }
    }

    private boolean isPodcast() {
        synchronized (this) {
            if (mCursor == null) {
                return false;
            }
            return (mCursor.getInt(PODCASTCOLIDX) > 0);
        }
    }

    private long getBookmark() {
        synchronized (this) {
            if (mCursor == null) {
                return 0;
            }
            return mCursor.getLong(BOOKMARKCOLIDX);
        }
    }

    /**
     * Returns the duration of the file in milliseconds.
     * Currently this method returns -1 for the duration of MIDI files.
     */
    public long duration() {
        if (mPlayer.isInitialized()) {
            return mPlayer.duration();
        }
        return -1;
    }

    /**
     * Returns the current playback position in milliseconds
     */
    public long position() {
        if (mPlayer.isInitialized()) {
            return mPlayer.position();
        }
        return -1;
    }

    /**
     * Seeks to the position specified.
     *
     * @param pos The position to seek to, in milliseconds
     */
    public long seek(long pos) {
        if (mPlayer.isInitialized()) {
            if (pos < 0) pos = 0;
            if (pos > mPlayer.duration()) pos = mPlayer.duration();
            return mPlayer.seek(pos);
        }
        return -1;
    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId: the audio session ID.
     */
    public void setAudioSessionId(int sessionId) {
        synchronized (this) {
            mPlayer.setAudioSessionId(sessionId);
        }
    }

    /**
     * Returns the audio session ID.
     */
    public int getAudioSessionId() {
        synchronized (this) {
            return mPlayer.getAudioSessionId();
        }
    }

    public void updateCurrentTrackInfo() {
        mCursor = getCursorForId(mPlaylist.getCurrentTrack());
        notifyChange(META_CHANGED);
    }

    /*
     * By making this a static class with a WeakReference to the Service, we
     * ensure that the Service can be GCd even when the system process still
     * has a remote reference to the stub.
     */
    static class ServiceStub extends IMediaPlaybackService.Stub {
        WeakReference<MediaPlaybackService> mService;

        ServiceStub(MediaPlaybackService service) {
            mService = new WeakReference<MediaPlaybackService>(service);
        }

        public void openFile(String path) {
            mService.get().open(path);
        }

        public void open(long[] list, int position) {
            mService.get().open(list, position);
        }

        public int getQueuePosition() {
            return mService.get().getQueuePosition();
        }

        public void setQueuePosition(int index) {
            mService.get().setQueuePosition(index);
        }

        public boolean isPlaying() {
            return mService.get().isPlaying();
        }

        public void stop() {
            mService.get().stop();
        }

        public void pause() {
            mService.get().pause();
        }

        public void play() {
            mService.get().play();
        }

        public void prev() {
            mService.get().prev();
        }

        public void next() {
            mService.get().gotoNext(true);
        }

        public String getTrackName() {
            return mService.get().getTrackName();
        }

        public String getAlbumName() {
            return mService.get().getAlbumName();
        }

        public long getAlbumId() {
            return mService.get().getAlbumId();
        }

        public String getArtistName() {
            return mService.get().getArtistName();
        }

        public long getArtistId() {
            return mService.get().getArtistId();
        }

        public void enqueue(long[] list, int action) {
            mService.get().enqueue(list, action);
        }

        public long[] getQueue() {
            return mService.get().getQueue();
        }

        public void moveQueueItem(int from, int to) {
            mService.get().moveQueueItem(from, to);
        }

        public String getPath() {
            return mService.get().getPath();
        }

        public long getAudioId() {
            return mService.get().getAudioId();
        }

        public long position() {
            return mService.get().position();
        }

        public long duration() {
            return mService.get().duration();
        }

        public long seek(long pos) {
            return mService.get().seek(pos);
        }

        public void setShuffleMode(int shufflemode) {
            mService.get().setShuffleMode(shufflemode);
        }

        public int getShuffleMode() {
            return mService.get().getShuffleMode();
        }

        public int removeTracks(int first, int last) {
            return mService.get().removeTracks(first, last);
        }

        public int removeTrack(long id) {
            return mService.get().removeTrack(id);
        }

        public void setRepeatMode(int repeatmode) {
            mService.get().setRepeatMode(repeatmode);
        }

        public int getRepeatMode() {
            return mService.get().getRepeatMode();
        }

        public int getMediaMountedCount() {
            return mService.get().getMediaMountedCount();
        }

        public int getAudioSessionId() {
            return mService.get().getAudioSessionId();
        }

        public void updateCurrentTrackInfo() {
            mService.get().updateCurrentTrackInfo();
        }

        public void removeFromQueue(long[] ids) {
            mService.get().removeTracks(ids);
        }
    }

    private final IBinder mBinder = new ServiceStub(this);

    //    @Override
//    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
//        writer.println("" + mPlaylist.size() + " items in queue, currently at index " + mPlaylist.getPlayPosition());
//        writer.println("Currently loaded:");
//        writer.println(getArtistName());
//        writer.println(getAlbumName());
//        writer.println(getTrackName());
//        writer.println(getPath());
//        writer.println("playing: " + mIsSupposedToBePlaying);
//        //writer.println("actual: " + mPlayer.mCurrentMediaPlayer.isPlaying());
//        writer.println("shuffle mode: " + mPlaylist.getShuffleMode());
//        MusicUtils.debugDump(writer);
////    }
}
