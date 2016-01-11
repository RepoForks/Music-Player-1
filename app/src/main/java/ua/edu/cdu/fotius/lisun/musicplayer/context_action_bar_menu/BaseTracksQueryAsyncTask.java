package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.AsyncTaskWithProgressBar;

public abstract class BaseTracksQueryAsyncTask extends AsyncTaskWithProgressBar {

    public interface Callbacks {
        public void onQueryStart();
        public void onQueryCompleted(long[] tracksId);
    }

    private WeakReference<Callbacks> mQueryCallbacksReference;

    public BaseTracksQueryAsyncTask(Fragment fragment, Callbacks callbacks) {
        super(fragment);
        mQueryCallbacksReference = new WeakReference<Callbacks>(callbacks);
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground();

        Context context = mFragmentWrapper.getActivity();
        if(context == null) return null;

        long[] trackIds = null;
        Callbacks queryCallbacks = mQueryCallbacksReference.get();
        if ((context != null) && (queryCallbacks != null)) {
            queryCallbacks.onQueryStart();
            long[] ids = (long[])params[0];
            trackIds = queryProvider(context, ids);
        }
        return trackIds;
    }

    @Override
    protected void onPostExecute(Object obj) {
        if(obj == null) return;

        long[] tracksIds = (long[])obj;
        Callbacks queryCallbacks = mQueryCallbacksReference.get();
        if (queryCallbacks != null) {
            queryCallbacks.onQueryCompleted(tracksIds);
        }
        super.onPostExecute(tracksIds);
    }

    protected abstract long[] queryProvider(Context context, long[] id);
}
