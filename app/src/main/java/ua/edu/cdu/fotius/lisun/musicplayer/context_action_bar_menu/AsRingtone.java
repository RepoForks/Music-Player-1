package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AsRingtone extends Command {

    public AsRingtone(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        if (idsOverWhichToExecute.length == 1) {
            long trackId = idsOverWhichToExecute[0];
            Uri uri = setAsRingtoneAndReturnUri(trackId);
            updateIsRingtoneField(uri);
            notifyUser(trackId);
        }
    }

    private Uri setAsRingtoneAndReturnUri(long id) {
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        RingtoneManager.setActualDefaultRingtoneUri(mFragment.getActivity(), RingtoneManager.TYPE_RINGTONE, uri);
        return uri;
    }

    private void updateIsRingtoneField(Uri uri) {
        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.Track.IS_RINGTONE, 1);
        mFragment.getActivity().getContentResolver().update(uri, values, null, null);
    }

    private void notifyUser(long id) {
        String[] cols = new String[]{
                AudioStorage.Track.TRACK_ID,
                AudioStorage.Track.TRACK
        };
        String whereClause = AudioStorage.Track.TRACK_ID + "=" + id;
        Cursor cursor = mFragment.getActivity().getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cols, whereClause, null, null);

        if(cursor != null) {
            if(cursor.getCount() == 1) {
                cursor.moveToFirst();
                int columnIdxTrackName = cursor.getColumnIndexOrThrow(AudioStorage.Track.TRACK);
                String message = mFragment.getResources()
                        .getString(R.string.ringtone_set, cursor.getString(columnIdxTrackName));
                Toast.makeText(mFragment.getActivity(), message, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }
}
