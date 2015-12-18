package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackDetalizationActivity;

public class OnPlaylistClickListener extends BaseFragmentItemClickListener{

    private String mPlaylistIdColumnName;
    private String mPlaylistColumnName;

    public OnPlaylistClickListener(Context context, CursorAdapter cursorAdapter,
                                   String playlistIdColumnName, String playlistColumnName) {
        super(context, cursorAdapter);
        mPlaylistIdColumnName = playlistIdColumnName;
        mPlaylistColumnName = playlistColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {

            int idColumnIndex = cursor.getColumnIndexOrThrow(mPlaylistIdColumnName);
            long playlistId = cursor.getLong(idColumnIndex);

            int playlistColumnIndex = cursor.getColumnIndexOrThrow(mPlaylistColumnName);
            String playlistName = cursor.getString(playlistColumnIndex);

            Bundle extras = new Bundle();
            extras.putLong(PlaylistsBrowserFragment.PLAYLIST_ID_KEY, playlistId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, playlistName);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
