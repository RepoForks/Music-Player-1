package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import java.util.HashMap;

public class QueueCursorAdapter extends BaseCursorAdapter {

    private final int INVALID_POSITION = -1;
    private HashMap<Long, Integer> mCursorTracksID;
    private long[] mCurrentTracksID;
    private String mTrackIdColumn;

    public QueueCursorAdapter(Context context, int layout, String[] from, int[] to, String trackIdColumn) {
        super(context, layout, from, to);
        mTrackIdColumn = trackIdColumn;
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        initIDs(c);
        return super.swapCursor(c);
    }

    private void initIDs(Cursor c) {
        if(c != null) {
            if(c.moveToFirst()) {
                int idx = 0;
                mCurrentTracksID = new long[c.getCount()];
                mCursorTracksID = new HashMap<>();
                while (!c.isAfterLast()) {
                    int columnIdx = c.getColumnIndexOrThrow(mTrackIdColumn);
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
        long tmp = mCurrentTracksID[from];
        mCurrentTracksID[from] = mCurrentTracksID[to];
        mCurrentTracksID[to] = tmp;
    }

    public long[] getCurrentQueue() {
        return mCurrentTracksID;
    }

    public HashMap<Long, Integer> getInitialIdToPositionMap() {
        return mCursorTracksID;
    }
}
