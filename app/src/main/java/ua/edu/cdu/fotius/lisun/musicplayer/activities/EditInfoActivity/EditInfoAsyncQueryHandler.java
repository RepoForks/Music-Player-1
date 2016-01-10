package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.AsyncFragmentWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class EditInfoAsyncQueryHandler extends AsyncQueryHandler {

    public interface QueryCallbacks{
        public void onQueryTrackInfoStarted();
        public void onQueryTrackInfoCompleted(Cursor c);
        public void onUpdateTrackInfoStarted();
        public void onUpdateTrackInfoCompleted();
    }

    private QueryCallbacks mQueryCallbacks;
    //TODO: move to method
    private EditInfoQueryCreator mEditInfoQueryCreator;
    private AsyncFragmentWrapper mFragmentWrapper;

    public EditInfoAsyncQueryHandler(Fragment fragment, EditInfoQueryCreator editInfoQueryCreator, QueryCallbacks queryCallbacks) {
        super(fragment.getActivity().getContentResolver());
        mQueryCallbacks = queryCallbacks;
        mEditInfoQueryCreator = editInfoQueryCreator;
        mFragmentWrapper = new AsyncFragmentWrapper(fragment);
    }

    public void queryTrackInfo() {
        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if(activity != null) {
            activity.showProgress();
        }

        super.startQuery(0, null,
                mEditInfoQueryCreator.getUri(),
                mEditInfoQueryCreator.getProjection(),
                mEditInfoQueryCreator.getSelection(),
                mEditInfoQueryCreator.getSelectionArgs(),
                mEditInfoQueryCreator.getSortOrder());
        mQueryCallbacks.onQueryTrackInfoStarted();
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        mQueryCallbacks.onQueryTrackInfoCompleted(cursor);

        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if(activity != null) {
            activity.hideProgress();
        }
    }

    public void updateTrackInfo(ContentValues contentValues) {
        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if(activity != null) {
            activity.showProgress();
        }

        super.startUpdate(0, null, mEditInfoQueryCreator.getUri(),
                contentValues, mEditInfoQueryCreator.getSelection(),
                mEditInfoQueryCreator.getSelectionArgs());
        mQueryCallbacks.onUpdateTrackInfoStarted();
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
        mQueryCallbacks.onUpdateTrackInfoCompleted();

        ToolbarActivity activity = mFragmentWrapper.getActivity();
        if(activity != null) {
            activity.hideProgress();
        }
    }
}
