package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.OnRenameDialogPositiveClick;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class PlaylistNameAsyncRetriever extends AsyncTaskWithProgressBar {

    private final String TAG = getClass().getSimpleName();

    private long mID;

    public PlaylistNameAsyncRetriever(Fragment fragment, long playlistID) {
        super(fragment);
        mID = playlistID;
    }

    @Override
    protected Object doInBackground(Object... params) {
        super.doInBackground(params);

        Context context = mFragmentWrapper.getActivity();
        if (context == null) return null;

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{AudioStorage.Playlist.PLAYLIST};
        String where = AudioStorage.Playlist.PLAYLIST_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(mID)};

        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(uri, projection, where, whereArgs, null);
        String playlist = null;
        if (c != null) {
            if(c.moveToFirst()) {
                playlist = c.getString(0);
            }
            c.close();
        }
        return playlist;
    }

    @Override
    protected void onPostExecute(Object obj) {
        String playlist = (String) obj;
        if (playlist != null) {
            ToolbarActivity activity = mFragmentWrapper.getActivity();
            if (activity == null) return;
            LayoutInflater inflater = activity.getLayoutInflater();

            createDialog(activity, inflater, playlist).show();
        }
        super.onPostExecute(obj);
    }

    public AlertDialog createDialog(Context c, LayoutInflater inflater, String playlist) {
        View v = inflater.inflate(R.layout.rename_dialog, null);
        EditTextWithValidation inputField = (EditTextWithValidation) v.findViewById(R.id.playlist_name);
        inputField.setInitialText(playlist);
        inputField.setSelection(playlist.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setView(v);
        builder.setTitle(c.getResources().getString(R.string.rename_dialog_title));
        builder.setPositiveButton(R.string.rename_dialog_positive_button,
                new OnRenameDialogPositiveClick(mFragmentWrapper.getFragment(), mID));
        builder.setNegativeButton(R.string.dialog_negative_button, new OnDialogNegativeClick());
        return builder.create();
    }
}

