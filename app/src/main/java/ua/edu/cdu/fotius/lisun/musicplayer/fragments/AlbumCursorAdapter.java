package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.internal.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 *
 */
public class AlbumCursorAdapter extends SimpleCursorAdapter {

    private final String TAG = getClass().getSimpleName();

    public AlbumCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, /*don't register content observer*/0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;
        final int albumTitleColumnIndex
                = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
        final int artistNameColumnIndex
                = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v == null) {
                continue;
            }

            String text = cursor.getString(from[i]);
            if (text == null) {
                text = "";
            } else if(text.equals(MediaStore.UNKNOWN_STRING)) {
                /*if artist name or album title is unknown
                * set string according to resource. Don't set
                * MediaStore.UNKNOWN_STRING, because now it's
                * <unknown>, but tomorrow can be <null>*/
                if(from[i] == albumTitleColumnIndex) {
                   text = context.getResources().getString(R.string.unknown_album);
                } else if(from[i] == artistNameColumnIndex){
                    text = context.getResources().getString(R.string.unknown_artist);
                }
            }

            if (v instanceof TextView) {
                setViewText((TextView) v, text);
            } else if (v instanceof ImageView) {
                setViewImage((ImageView) v, text);
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this SimpleCursorAdapter");
            }
        }
    }
}
