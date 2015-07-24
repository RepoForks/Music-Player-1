package ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters;

import android.content.Context;
import android.database.Cursor;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsBrowserFragment;

/**
 *
 */
public class ArtistSimpleCursorAdapter extends BaseSimpleCursorAdapter{

    public ArtistSimpleCursorAdapter(Context context, int layout, String[] from, int[] to) {
        super(context, layout, from, to);
    }

    @Override
    public String getUnknownText(Context context, Cursor cursor, int columnIndex) {

        if(columnIndex == cursor.getColumnIndexOrThrow(ArtistsBrowserFragment.ARTIST_COLUMN_NAME)) {
            return context.getResources().getString(R.string.unknown_artist);
        }
        return context.getResources().getString(R.string.undefined_unknown);
    }
}
