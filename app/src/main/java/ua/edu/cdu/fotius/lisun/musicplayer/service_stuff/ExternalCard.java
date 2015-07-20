package ua.edu.cdu.fotius.lisun.musicplayer.service_stuff;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by andrei on 18.07.2015.
 */
public class ExternalCard {

    private int mCardId;

    public ExternalCard(Context context) {
        ContentResolver res = context.getContentResolver();
        Cursor c = res.query(Uri.parse("content://media/external/fs_id"), null, null, null, null);
        int id = -1;
        if (c != null) {
            c.moveToFirst();
            id = c.getInt(0);
            c.close();
        }
        mCardId = id;

    }

    public int get() {
        return mCardId;
    }

    public void set(int mCardId) {
        this.mCardId = mCardId;
    }
}
