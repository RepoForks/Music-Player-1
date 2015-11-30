package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.BaseActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackDetalizationActivity;

public class OnAlbumClickListener extends BaseFragmentItemClickListener{

    private final String TAG = getClass().getSimpleName();

    private Bundle mArgumentsPassedToFragment;
    private String mAlbumIdColumnName;
    private String mAlbumTitleColumnName;

    public OnAlbumClickListener(Context context, CursorAdapter cursorAdapter,
                                Bundle previousActivityExtras,
                                String albumIdColumnName,
                                String albumTitleColumnName) {
        super(context, cursorAdapter);
        mArgumentsPassedToFragment = previousActivityExtras;
        mAlbumIdColumnName = albumIdColumnName;
        mAlbumTitleColumnName = albumTitleColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(mAlbumIdColumnName);
            long albumId = cursor.getLong(idColumnIndex);

            int titleColumnIndex = cursor.getColumnIndexOrThrow(mAlbumTitleColumnName);
            String albumTitle = cursor.getString(titleColumnIndex);

            //if AlbumsBrowserFragment was called from ArtistBrowserFragment
            Bundle extras = mArgumentsPassedToFragment;

            if(extras == null) {
                extras = new Bundle();
            }

            extras.putLong(AlbumsBrowserFragment.ALBUM_ID_KEY, albumId);
            extras.putString(BaseActivity.TOOLBAR_TITLE_KEY, albumTitle);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
