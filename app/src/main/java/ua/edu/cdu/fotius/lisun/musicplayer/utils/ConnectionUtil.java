package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class ConnectionUtil {

    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        return ((activeNetwork != null) && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                && (activeNetwork.isConnected()));
    }
}
