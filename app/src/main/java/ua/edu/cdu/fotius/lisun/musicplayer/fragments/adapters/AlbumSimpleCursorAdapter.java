package ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters;

import android.content.Context;
import android.database.Cursor;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.BaseSimpleCursorAdapter;

/**
 * Fragment specific Adapter and template method
 * {@link ua.edu.cdu.fotius.lisun.musicplayer.fragments.adapters.AlbumSimpleCursorAdapter
 * #getUnknownText(android.content.Context, android.database.Cursor, int)}
 */
public class AlbumSimpleCursorAdapter extends BaseSimpleCursorAdapter {

    public AlbumSimpleCursorAdapter(Context context, int layout, String[] from, int[] to) {
        super(context, layout, from, to);
    }

    @Override
    public String getUnknownText(final Context context,
                                 final Cursor cursor, final int columnIndex) {

        if(columnIndex ==
                cursor.getColumnIndexOrThrow(AlbumsBrowserFragment.ALBUM_TITLE_COLUMN)) {
            return context.getResources().getString(R.string.unknown_album);
        } else if(columnIndex == cursor.getColumnIndexOrThrow(AlbumsBrowserFragment.ARTIST_NAME_COLUMN)){
            return context.getResources().getString(R.string.unknown_artist);
        }
        //if we weren't able to define what exactly
        //is unknown
        return context.getResources().getString(R.string.undefined_unknown);
    }
}
