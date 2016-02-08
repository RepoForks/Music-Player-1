package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;

import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.EditInfoQueryCreator;

public class UpdateTrackInfoAsyncTask extends AsyncTaskWithProgressBar{

    public interface Callback {
        public void updateCompleted();
    }

    private WeakReference<Callback> mCallback;
    private EditInfoQueryCreator mQueryCreator;
    private ContentValues mContentValues;

    public UpdateTrackInfoAsyncTask(Fragment fragment, ContentValues contentValues, EditInfoQueryCreator queryCreator, Callback callback) {
        super(fragment);
        mCallback = new WeakReference<Callback>(callback);
        mQueryCreator = queryCreator;
        mContentValues = contentValues;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);
        Context context = mFragmentWrapper.getActivity();
        if(context == null) {
            return null;
        }
        context.getContentResolver().update(mQueryCreator.getUri(),
                mContentValues, mQueryCreator.getSelection(),
                mQueryCreator.getSelectionArgs());
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Callback callback = mCallback.get();
        if(callback != null) {
            callback.updateCompleted();
        }
        super.onPostExecute(o);
    }
}
