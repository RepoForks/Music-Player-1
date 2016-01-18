package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TracksActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsFragment;

public class OnAlbumClickListener extends BaseFragmentItemClickListener{

    private Bundle mArgumentsPassedToFragment;
    private String mAlbumIdColumn;
    private String mAlbumTitleColumn;

    public OnAlbumClickListener(Context context, CursorAdapter cursorAdapter,
                                Bundle previousActivityExtras,
                                String albumIdColumn,
                                String albumTitleColumn) {
        super(context, cursorAdapter);
        mArgumentsPassedToFragment = previousActivityExtras;
        mAlbumIdColumn = albumIdColumn;
        mAlbumTitleColumn = albumTitleColumn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(mAlbumIdColumn);
            long albumId = cursor.getLong(idColumnIndex);

            int titleColumnIndex = cursor.getColumnIndexOrThrow(mAlbumTitleColumn);
            String albumTitle = cursor.getString(titleColumnIndex);

            //if AlbumsBrowserFragment was called from ArtistBrowserFragment
            Bundle extras = mArgumentsPassedToFragment;

            if(extras == null) {
                extras = new Bundle();
            }

            extras.putLong(AlbumsFragment.ALBUM_ID_KEY, albumId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, albumTitle);
            Intent intent = new Intent(mContext, TracksActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
