package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

public class EditInfoAsyncQueryHandler extends AsyncQueryHandler {

    public interface QueryCallbacks{
        public void onQueryTrackInfoStarted();
        public void onQueryTrackInfoCompleted(Cursor c);
    }

    private QueryCallbacks mQueryCallbacks;
    private EditInfoQueryCreator mEditInfoQueryCreator;

    public EditInfoAsyncQueryHandler(ContentResolver cr, EditInfoQueryCreator editInfoQueryCreator, QueryCallbacks queryCallbacks) {
        super(cr);
        mQueryCallbacks = queryCallbacks;
        mEditInfoQueryCreator = editInfoQueryCreator;
    }

    public void queryTrackInfo() {
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
    }
}
