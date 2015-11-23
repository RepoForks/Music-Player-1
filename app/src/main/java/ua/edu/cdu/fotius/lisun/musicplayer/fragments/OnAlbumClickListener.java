package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.TrackDetalizationActivity;

public class OnAlbumClickListener extends BaseFragmentItemClickListener{

    private final String TAG = getClass().getSimpleName();

    private Bundle mArgumentsPassedToFragment;
    private String mAlbumIdColumnName;

    public OnAlbumClickListener(Context context, CursorAdapter cursorAdapter, Bundle previousActivityExtras, String albumIdColumnName) {
        super(context, cursorAdapter);
        mArgumentsPassedToFragment = previousActivityExtras;
        mAlbumIdColumnName = albumIdColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(mAlbumIdColumnName);
            long albumId = cursor.getLong(idColumnIndex);

            //if AlbumsBrowserFragment was called from ArtistBrowserFragment
            Bundle extras = mArgumentsPassedToFragment;

            if(extras == null) {
                extras = new Bundle();
            }

            extras.putLong(AlbumsBrowserFragment.ALBUM_ID_KEY, albumId);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
