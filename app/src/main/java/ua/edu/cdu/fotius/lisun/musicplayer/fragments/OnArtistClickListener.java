package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.AlbumsDetalizationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;

public class OnArtistClickListener extends BaseFragmentItemClickListener {

    private String mArtistIdColumnName;
    private String mArtistColumnName;

    public OnArtistClickListener(Context context, CursorAdapter cursorAdapter,
                                 String artistIdColumnName, String artistColumnName) {
        super(context, cursorAdapter);
        mArtistIdColumnName = artistIdColumnName;
        mArtistColumnName = artistColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if ((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex =
                    cursor.getColumnIndexOrThrow(mArtistIdColumnName);
            long artistId = cursor.getLong(idColumnIndex);

            int artistColumnIndex =
                    cursor.getColumnIndexOrThrow(mArtistColumnName);
            String artistName = cursor.getString(artistColumnIndex);

            Bundle extras = new Bundle();
            extras.putLong(ArtistsBrowserFragment.ARTIST_ID_KEY, artistId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, artistName);
            Intent intent = new Intent(mContext, AlbumsDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
