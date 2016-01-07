package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.IndeterminateProgressBarManager;

public abstract class BaseTracksQueryAsyncTask extends AsyncTask<long[], Void, long[]>{
    public interface Callbacks {
        public void onQueryStart();
        public void onQueryCompleted(long[] tracksId);
    }

    private WeakReference<Context> mContextReference;
    private WeakReference<Callbacks> mQueryCallbacksReference;
    private IndeterminateProgressBarManager mProgressBar;

    public BaseTracksQueryAsyncTask(Context context, Callbacks callbacks) {
        mContextReference = new WeakReference<Context>(context);
        mQueryCallbacksReference = new WeakReference<Callbacks>(callbacks);
        mProgressBar = new IndeterminateProgressBarManager(context);
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
