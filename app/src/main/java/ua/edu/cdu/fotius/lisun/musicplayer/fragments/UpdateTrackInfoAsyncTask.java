package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.AsyncTaskWithProgressBar;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditInfoQueryCreator;

public class UpdateTrackInfoAsyncTask extends AsyncTaskWithProgressBar{

    public interface Callback {
        public void updateCompleted();
    }

    private Callback mCallback;
    private EditInfoQueryCreator mQueryCreator;
    private ContentValues mContentValues;

    public UpdateTrackInfoAsyncTask(Fragment fragment, ContentValues contentValues, EditInfoQueryCreator queryCreator, Callback callback) {
        super(fragment);
        mCallback = callback;
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
        super.onPostExecute(o);
        mCallback.updateCompleted();
    }
}
