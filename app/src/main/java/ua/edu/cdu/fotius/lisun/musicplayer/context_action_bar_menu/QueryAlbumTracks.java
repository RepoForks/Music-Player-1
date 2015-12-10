package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class QueryAlbumTracks extends AsyncTask<long[], Void, long[]>{

    private final String TAG = getClass().getSimpleName();

    //TODO: all queries's callbacks move to super class
    public interface Callbacks {
        public void onQueryStart();
        public void onQueryCompleted(long[] trackIds);
    }

    private WeakReference<Context> mContextReference;
    private WeakReference<Callbacks> mQueryCallbacksReference;

    public QueryAlbumTracks(Context context, Callbacks callbacks) {
        mContextReference = new WeakReference<Context>(context);
        mQueryCallbacksReference = new WeakReference<Callbacks>(callbacks);
    }

    @Override
    protected long[] doInBackground(long[]... params) {
        long[] trackIds = null;
        Context context = mContextReference.get();
        Callbacks queryCallbacks = mQueryCallbacksReference.get();
        if ((context != null) && (queryCallbacks != null)) {
            queryCallbacks.onQueryStart();
            long[] albumIDs = params[0];
            trackIds = DatabaseUtils.queryAlbumsTracks(context, albumIDs);
        }
        return trackIds;
    }

    @Override
    protected void onPostExecute(long[] tracksIds) {
        super.onPostExecute(tracksIds);
        Callbacks queryCallbacks = mQueryCallbacksReference.get();
        if(queryCallbacks != null) {
            queryCallbacks.onQueryCompleted(tracksIds);
        }
    }
}
