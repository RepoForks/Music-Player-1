package ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

//TODO: Maybe no need to load all to cursor
//but only 10-20
public class MediaContentResolver {

    public static Cursor query(Context context, Uri uri, String[] projection,
                               String selection, String[] selectionArgs, String sortOrder) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }

    }
}
