package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class EditInfoAsyncQueryHandler extends AsyncQueryHandler{

    public EditInfoAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
    }
}
