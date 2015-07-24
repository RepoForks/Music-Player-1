package ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters;

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
 *An easy adapter to map columns from a cursor to TextViews or ImageViews defined in an XML file.
 * Template method pattern is used to define fragment specific behaviour, such as getting unknown
 * string({@link ua.edu.cdu.fotius.lisun.musicplayer.fragments
 * .BaseSimpleCursorAdapter#getUnknownText(android.content.Context, android.database.Cursor, int)})
 */
public abstract class BaseSimpleCursorAdapter extends SimpleCursorAdapter {

    public BaseSimpleCursorAdapter(Context context, int layout, String[] from, int[] to) {
        super(context, layout, /*cursor*/null, from, to, /*don't register content observer*/0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v == null) {
                continue;
            }

            String text = cursor.getString(from[i]);
            if (text == null) {
                text = "";
            } else if(text.equals(MediaStore.UNKNOWN_STRING)) {
                text = getUnknownText(context, cursor, from[i]);
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

    public abstract String getUnknownText(final Context context,
                                          final Cursor cursor, final int columnIndex);
}
