package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by andrei on 04.08.2015.
 */
public class PlaybackServiceConnection implements ServiceConnection{

    private Context mContext;

    public PlaybackServiceConnection(Context context) {
        mContext = context;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
