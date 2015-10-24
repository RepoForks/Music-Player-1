package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.TrackDetalizationActivity;

public class OnPlaylistClick extends BaseFragmentItemClickListener{

    public OnPlaylistClick(Context context, CursorAdapter cursorAdapter) {
        super(context, cursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST_ID);
            long playlistId = cursor.getLong(idColumnIndex);
            Bundle bundle = new Bundle();
            bundle.putLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, playlistId);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}
