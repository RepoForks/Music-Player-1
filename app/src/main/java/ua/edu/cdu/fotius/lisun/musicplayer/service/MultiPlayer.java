package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;

/**
 * Provides a unified interface for dealing with midi files and
 * other media files.
 */
public class MultiPlayer {

    private CompatMediaPlayer mCurrentMediaPlayer = new CompatMediaPlayer();
    private CompatMediaPlayer mNextMediaPlayer;
    private Handler mHandler;
    private boolean mIsInitialized = false;
    private Context mContext;

    public MultiPlayer(Context context) {
        mContext = context;
        //CPU should be running. Screen is allowed to go off
        //So can be played with offed screen
        mCurrentMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
    }

    /**
     * Set current player(mCurrentMediaPlayer) with new data source or if failes
     * next player(mNextMediaPlayer)
     * @param path
     */
    public void setDataSource(String path) {
        //trying to set mCurrentMediaPlayer with new data source
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
        //if attept failed trying to set mNextMediaPlayer
        //with newDataSource
        if (mIsInitialized) {
            setNextDataSource(null);
        }
    }

    private boolean setDataSourceImpl(MediaPlayer player, String path) {
        try {
            player.reset();
            player.setOnPreparedListener(null);
            //if path is in URI representation
            if (path.startsWith("content://")) {
                player.setDataSource(mContext, Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
        } catch (IOException ex) {
            // TODO: notify the user why the file couldn't be opened
            return false;
        } catch (IllegalArgumentException ex) {
            // TODO: notify the user why the file couldn't be opened
            return false;
        }
        //listener.onCompletion() will be called from
        //player.onCompletion()
        player.setOnCompletionListener(listener);
        player.setOnErrorListener(errorListener);

        //TODO: do I actually need this here
        //audio session is opened and requires audio effects to be applied.
        Intent i = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, mContext.getPackageName());
        mContext.sendBroadcast(i);
        return true;
    }

    /**
     * Creates new CompatMediaPlayer for mNextMediaPlayer
     * and call {@link #setDataSourceImpl(android.media.MediaPlayer, String)}
     * on it
     * @param path
     */
    public void setNextDataSource(String path) {
        //indicate no next player should be started at the end of playback
        mCurrentMediaPlayer.setNextMediaPlayer(null);
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer.release();
            mNextMediaPlayer = null;
        }
        if (path == null) {
            return;
        }
        mNextMediaPlayer = new CompatMediaPlayer();
        mNextMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
        mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
        if (setDataSourceImpl(mNextMediaPlayer, path)) {
            mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
        } else {
            // failed to open next, we'll transition the old fashioned way,
            // which will skip over the faulty file
            mNextMediaPlayer.release();
            mNextMediaPlayer = null;
        }
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public void start() {
        mCurrentMediaPlayer.start();
    }

    public void stop() {
        mCurrentMediaPlayer.reset();
        mIsInitialized = false;
    }

    /**
     * You CANNOT use this player anymore after calling release()
     */
    public void release() {
        stop();
        mCurrentMediaPlayer.release();
    }

    public void pause() {
        mCurrentMediaPlayer.pause();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = mNextMediaPlayer;
                mNextMediaPlayer = null;
                mHandler.sendEmptyMessage(MediaPlaybackService.TRACK_WENT_TO_NEXT);
            } else {
                // Acquire a temporary wakelock, since when we return from
                // this callback the MediaPlayer will release its wakelock
                // and allow the device to go to sleep.
                // This temporary wakelock is released when the RELEASE_WAKELOCK
                // message is processed, but just in case, put a timeout on it.
                mHandler.sendEmptyMessage(MediaPlaybackService.ACQUIRE_WAKELOCK);
                mHandler.sendEmptyMessage(MediaPlaybackService.TRACK_ENDED);
                mHandler.sendEmptyMessage(MediaPlaybackService.RELEASE_WAKELOCK);
            }
        }
    };

    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    mIsInitialized = false;
                    mCurrentMediaPlayer.release();
                    // Creating a new MediaPlayer and settings its wakemode does not
                    // require the media service, so it's OK to do this now, while the
                    // service is still being restarted
                    mCurrentMediaPlayer = new CompatMediaPlayer();
                    mCurrentMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MediaPlaybackService.SERVER_DIED), 2000);
                    return true;
                default:
                    Log.d("MultiPlayer", "Error: " + what + "," + extra);
                    break;
            }
            return false;
        }
    };

    public long duration() {
        return mCurrentMediaPlayer.getDuration();
    }

    public long position() {
        return mCurrentMediaPlayer.getCurrentPosition();
    }

    public long seek(long whereto) {
        mCurrentMediaPlayer.seekTo((int) whereto);
        return whereto;
    }

    public void setVolume(float vol) {
        mCurrentMediaPlayer.setVolume(vol, vol);
    }

    public void setAudioSessionId(int sessionId) {
        mCurrentMediaPlayer.setAudioSessionId(sessionId);
    }

    public int getAudioSessionId() {
        return mCurrentMediaPlayer.getAudioSessionId();
    }
}
