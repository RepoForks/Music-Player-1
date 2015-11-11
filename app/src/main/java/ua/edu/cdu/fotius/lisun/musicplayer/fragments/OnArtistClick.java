package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.AlbumsDetalizationActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class OnArtistClick extends BaseFragmentItemClickListener{

    public OnArtistClick(Context context, CursorAdapter cursorAdapter) {
       super(context, cursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if ((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex =
                    cursor.getColumnIndexOrThrow(AudioStorage.Artist.ARTIST_ID);
            long artistId = cursor.getLong(idColumnIndex);
            Bundle extras = new Bundle();
            extras.putInt(AlbumsDetalizationActivity.CALLED_FROM_KEY, AlbumsDetalizationActivity.CALLED_FROM_ARTISTS);
            extras.putLong(ArtistsBrowserFragment.ARTIST_ID_KEY, artistId);
            Intent intent = new Intent(mContext, AlbumsDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
