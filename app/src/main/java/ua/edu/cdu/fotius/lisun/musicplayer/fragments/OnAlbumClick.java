package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.TrackDetalizationActivity;

public class OnAlbumClick extends BaseFragmentItemClickListener{

    public OnAlbumClick(Context context, CursorAdapter cursorAdapter) {
        super(context, cursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.Album.ALBUM_ID);
            long albumId = cursor.getLong(idColumnIndex);
            Bundle extras = new Bundle();
            extras.putInt(TrackDetalizationActivity.CALLED_FROM_KEY,
                    TrackDetalizationActivity.CALLED_FROM_ALBUMS);
            extras.putLong(AlbumsBrowserFragment.ALBUM_ID_KEY, albumId);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
