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

public class OnGenreClickListener extends BaseFragmentItemClickListener{

    //TODO: maybe i don't need this, just hardcode these values
    private String mGenreIdColumnName;
    private String mGenreColumnName;

    public OnGenreClickListener(Context context,
                                CursorAdapter cursorAdapter, String genreIdColumnName, String genreColumnName) {
        super(context, cursorAdapter);
        mGenreIdColumnName = genreIdColumnName;
        mGenreColumnName = genreColumnName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        if((cursor != null) && (cursor.moveToPosition(position))) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(mGenreIdColumnName);
            long genreId = cursor.getLong(idColumnIndex);

            int titleColumnIndex = cursor.getColumnIndexOrThrow(mGenreColumnName);
            String genreTitle = cursor.getString(titleColumnIndex);

            Bundle extras = new Bundle();
            extras.putLong(GenresFragment.GENRE_ID_KEY, genreId);
            extras.putString(ToolbarActivity.TOOLBAR_TITLE_KEY, genreTitle);
            Intent intent = new Intent(mContext, TrackDetalizationActivity.class);
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
