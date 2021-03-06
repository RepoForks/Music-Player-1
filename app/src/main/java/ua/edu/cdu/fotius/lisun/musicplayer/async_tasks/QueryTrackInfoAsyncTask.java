package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.EditInfoQueryCreator;

public class QueryTrackInfoAsyncTask extends AsyncTaskWithProgressBar{

    public interface Callback {
        public void queryCompleted(Cursor c);
    }

    /*Callback can be Fragment or Activity
    which can be destroyed anytime*/
    private WeakReference<Callback> mCallback;
    private EditInfoQueryCreator mQueryCreator;

    public QueryTrackInfoAsyncTask(Fragment fragment, EditInfoQueryCreator queryCreator, Callback callback) {
        super(fragment);
        mCallback = new WeakReference<Callback>(callback);
        mQueryCreator = queryCreator;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);
        Context context = mFragmentWrapper.getActivity();
        if(context == null) {
            return null;
        }

        Cursor c = context.getContentResolver().query(mQueryCreator.getUri(),
                mQueryCreator.getProjection(),
                mQueryCreator.getSelection(),
                mQueryCreator.getSelectionArgs(),
                mQueryCreator.getSortOrder());
        return c;
    }

    @Override
    protected void onPostExecute(Object obj) {
        Callback callback = mCallback.get();
        if(callback != null) {
            callback.queryCompleted((Cursor) obj);
        }
        super.onPostExecute(obj);
    }
}
