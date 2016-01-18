package ua.edu.cdu.fotius.lisun.musicplayer.listeners;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.PlaylistTracksActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.PlaylistsFragment;

public class OnPlaylistClickListener extends BaseFragmentItemClickListener{



    private String mPlaylistIdColumn;
    private String mPlaylistColumn;

    public OnPlaylistClickListener(Context context, CursorAdapter cursorAdapter,
                                   String playlistIdColumn, String playlistColumn) {
        super(context, cursorAdapter);
        mPlaylistIdColumn = playlistIdColumn;
        mPlaylistColumn = playlistColumn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {

            //TODO: make async!!!
            int idColumnIndex = cursor.getColumnIndexOrThrow(mPlaylistIdColumn);
            long playlistId = cursor.getLong(idColumnIndex);

            int playlistColumnIndex = cursor.getColumnIndexOrThrow(mPlaylistColumn);
            String playlistName = cursor.getString(playlistColumnIndex);

            Bundle extras = new Bundle();
            extras.putLong(PlaylistsFragment.PLAYLIST_ID_KEY, playlistId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, playlistName);
            Intent intent = new Intent(mContext, PlaylistTracksActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
