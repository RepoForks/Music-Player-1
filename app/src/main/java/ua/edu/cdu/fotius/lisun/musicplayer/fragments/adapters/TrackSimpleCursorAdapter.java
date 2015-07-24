package ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters;

import android.content.Context;
import android.database.Cursor;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;

/**
 * Fragment specific Adapter and template method
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.AlbumSimpleCursorAdapter
 * #getUnknownText(android.content.Context, android.database.Cursor, int)}
 */
public class TrackSimpleCursorAdapter extends BaseSimpleCursorAdapter{

    public TrackSimpleCursorAdapter(Context context, int layout, String[] from, int[] to) {
        super(context, layout, from, to);
    }

    @Override
    public String getUnknownText(Context context, Cursor cursor, int columnIndex) {

        if(columnIndex == cursor.getColumnIndexOrThrow(TrackBrowserFragment.ARTIST_TITLE_COLUMN)) {
            return context.getResources().getString(R.string.unknown_artist);
        }
        //if we weren't able to define what exactly
        //is unknown
        return context.getResources().getString(R.string.undefined_unknown);
    }
}
