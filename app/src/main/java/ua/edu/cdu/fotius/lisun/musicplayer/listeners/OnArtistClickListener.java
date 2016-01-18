package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.AlbumsActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.ArtistsFragment;

public class OnArtistClickListener extends BaseFragmentItemClickListener {

    private String mArtistIdColumn;
    private String mArtistColumn;

    public OnArtistClickListener(Context context, CursorAdapter cursorAdapter,
                                 String artistIdColumn, String artistColumn) {
        super(context, cursorAdapter);
        mArtistIdColumn = artistIdColumn;
        mArtistColumn = artistColumn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if ((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex =
                    cursor.getColumnIndexOrThrow(mArtistIdColumn);
            long artistId = cursor.getLong(idColumnIndex);

            int artistColumnIndex =
                    cursor.getColumnIndexOrThrow(mArtistColumn);
            String artistName = cursor.getString(artistColumnIndex);

            Bundle extras = new Bundle();
            extras.putLong(ArtistsFragment.ARTIST_ID_KEY, artistId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, artistName);
            Intent intent = new Intent(mContext, AlbumsActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
