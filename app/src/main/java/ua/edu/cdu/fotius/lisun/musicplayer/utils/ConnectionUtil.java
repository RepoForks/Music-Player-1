package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionUtil {

    public static boolean isConnectedToInternet(Context context) {
        NetworkInfo activeNetwork = getNetworkInfo(context);
        Log.d(ConnectionUtil.class.getSimpleName(), "isConnectedToInternet. " +
                ((activeNetwork != null) && (activeNetwork.isConnected())));
        return ((activeNetwork != null) && (activeNetwork.isConnected()));
    }


    public static boolean isConnectedToWifi(Context context) {
        NetworkInfo activeNetwork = getNetworkInfo(context);
        return ((activeNetwork != null) && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                && (activeNetwork.isConnected()));
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo();
    }
}
