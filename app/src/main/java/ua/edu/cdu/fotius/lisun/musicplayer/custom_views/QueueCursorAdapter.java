package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import ua.edu.cdu.fotius.lisun.musicplayer.fragments.BaseSimpleCursorAdapter;

public class QueueCursorAdapter extends BaseSimpleCursorAdapter {

    private final String TAG = getClass().getSimpleName();

    private final int INVALID_POSITION = -1;
    private HashMap<Long, Integer> mCursorTracksID;
    private long[] mCurrentTracksID;

    public QueueCursorAdapter(Context context, int layout, String[] from, int[] to) {
        super(context, layout, from, to);
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        initIDs(c);
        Log.d(TAG, "Queue array length: " + mCurrentTracksID.length);
        return super.swapCursor(c);
    }

    private void initIDs(Cursor c) {
        if(c != null) {
            if(c.moveToFirst()) {
                int idx = 0;
                mCurrentTracksID = new long[c.getCount()];
                mCursorTracksID = new HashMap<>();
                while (!c.isAfterLast()) {
                    int columnIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    long trackID = c.getLong(columnIdx);
                    mCurrentTracksID[idx] = trackID;
                    mCursorTracksID.put(trackID, idx);
                    idx++;
                    c.moveToNext();
                }
            }
        }
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        Log.d(TAG, "QueueCursorsAdapter. bindView");
        int cursorPosition = cursor.getPosition();
        long trackIDPosition = mCurrentTracksID[cursorPosition];
        int newCursorPosition = mCursorTracksID.get(trackIDPosition);
        cursor.moveToPosition(newCursorPosition);
        super.bindView(rowLayout, context, cursor);
    }

    public long getIDForPosition(int position) {
        if((position < 0) || (position >= mCurrentTracksID.length)) {
            return INVALID_POSITION;
        }
        return mCurrentTracksID[position];
    }

    public void swapItems(int from, int to) {
        Log.d(TAG, "swapItems. from: " + from + " to: " + to);
        long tmp = mCurrentTracksID[from];
        mCurrentTracksID[from] = mCurrentTracksID[to];
        mCurrentTracksID[to] = tmp;
    }

    public long[] getCurrentQueue() {
        return mCurrentTracksID;
    }
}
