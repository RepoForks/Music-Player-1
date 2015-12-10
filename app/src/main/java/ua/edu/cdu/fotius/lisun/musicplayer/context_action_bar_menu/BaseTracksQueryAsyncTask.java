package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class BaseTracksQueryAsyncTask extends AsyncTask<long[], Void, long[]>{
    public interface Callbacks {
        public void onQueryStart();
        public void onQueryCompleted(long[] trackIds);
    }

    private WeakReference<Context> mContextReference;
    private WeakReference<Callbacks> mQueryCallbacksReference;

    public BaseTracksQueryAsyncTask(Context context, Callbacks callbacks) {
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
            long[] ids = params[0];
            trackIds = queryProvider(context, ids);
        }
        return trackIds;
    }

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onPostExecute(long[] tracksIds) {
        super.onPostExecute(tracksIds);
        Callbacks queryCallbacks = mQueryCallbacksReference.get();
        if(queryCallbacks != null) {
            queryCallbacks.onQueryCompleted(tracksIds);
        }
    }

    protected abstract long[] queryProvider(Context context, long[] id);
}
