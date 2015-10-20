package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class Delete extends Command{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private long[] mTrackIdsOverWhichToExecute;

    public Delete(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(serviceWrapper);
        mContext = context;
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mTrackIdsOverWhichToExecute = idsOverWhichToExecute;
        showConfirmationDialog();
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        Resources resources = mContext.getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        alertBuilder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message)
                .setPositiveButton(R.string.delete_dialog_positive_button, mPositiveButtonListener)
                .setNegativeButton(R.string.dialog_negative_button, mNegativeButtonListener);
        alertBuilder.create().show();
    }

    private DialogInterface.OnClickListener mPositiveButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String[] cols = new String[] {
                    AudioStorage.Track.TRACK_ID, AudioStorage.Track.FILE_PATH
            };
            String whereClause = getWhereClause();
            ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cols, whereClause, null, null);
            if(cursor != null) {
                deleteFromCurrentPlaylist(cursor);
                deleteFromStorage(cursor);
                deleteFromDatabase(resolver, whereClause);
                cursor.close();
                mContext.getContentResolver().notifyChange(Uri.parse("content://media"), null);
            }
        }
    };

    private String getWhereClause() {
        StringBuffer whereClause = new StringBuffer();
        whereClause.append(AudioStorage.Track.TRACK_ID + " IN (");
        for(int i = 0; i < mTrackIdsOverWhichToExecute.length; i++) {
            whereClause.append(mTrackIdsOverWhichToExecute[i]);
            if(i < mTrackIdsOverWhichToExecute.length - 1) {
                whereClause.append(",");
            }
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    private void deleteFromCurrentPlaylist(Cursor cursor) {
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                long trackId = cursor.getLong(0);
                mServiceWrapper.removeTrack(trackId);
                cursor.moveToNext();
            }
        }
    }

    private void deleteFromStorage(Cursor cursor) {
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String path = cursor.getString(1);
                File f = new File(path);
                f.delete();
                cursor.moveToNext();
            }
        }
    }

    private void deleteFromDatabase(ContentResolver resolver, String where) {
        resolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, where, null);
    }

    private DialogInterface.OnClickListener mNegativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
}
