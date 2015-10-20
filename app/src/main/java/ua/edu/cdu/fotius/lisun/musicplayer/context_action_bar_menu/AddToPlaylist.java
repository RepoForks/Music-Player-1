package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AddToPlaylist extends Command{

    private final String TAG = getClass().getSimpleName();

    //TODO: Refactor: Move this up to super
    private Context mContext;
    private ArrayList<PlaylistNameIdTuple> mAvailablePlaylists;
    private long[] mTrackIdsOverWhichToExecute;

    public AddToPlaylist(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(serviceWrapper);
        mContext = context;
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mTrackIdsOverWhichToExecute = idsOverWhichToExecute;
        mAvailablePlaylists = queryPlaylists();
        String[] playlistsNames = retreivePlaylistsNames(mAvailablePlaylists);
        showDialog(playlistsNames);
    }

    private ArrayList<PlaylistNameIdTuple> queryPlaylists() {
        ContentResolver resolver = mContext.getContentResolver();
        String[] cols = new String[] {
                AudioStorage.UserPlaylist.PLAYLIST_ID,
                AudioStorage.UserPlaylist.PLAYLIST
        };

        ArrayList<PlaylistNameIdTuple> playlists = new ArrayList<PlaylistNameIdTuple>();
        Cursor cursor = resolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cols, null, null, null);
        if((cursor != null) && (cursor.moveToFirst())) {
            int idIdx = cursor.getColumnIndexOrThrow(AudioStorage.UserPlaylist.PLAYLIST_ID);
            int nameIdx = cursor.getColumnIndexOrThrow(AudioStorage.UserPlaylist.PLAYLIST);
            while(!cursor.isAfterLast()) {
                playlists.add(new PlaylistNameIdTuple(cursor.getLong(idIdx),
                        cursor.getString(nameIdx)));
                cursor.moveToNext();
            }
        }

        return playlists;
    }

    private String[] retreivePlaylistsNames(ArrayList<PlaylistNameIdTuple> playlists) {
        String[] playlistsNames = new String[playlists.size()];
        for(int i = 0; i < playlists.size(); i++) {
            playlistsNames[i] = playlists.get(i).getName();
        }
        return playlistsNames;
    }

    private void showDialog(String[] playlistsNames) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setNegativeButton(R.string.dialog_negative_button, mOnNegativeButtonClick);
        dialogBuilder.setItems(playlistsNames, mOnDialogListItemClick);
        dialogBuilder.create().show();
    }

    private DialogInterface.OnClickListener mOnNegativeButtonClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener mOnDialogListItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            ContentResolver resolver = mContext.getContentResolver();
            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", mAvailablePlaylists.get(which).getId());
            Cursor cursor = resolver.query(uri, null, null, null, null);
            int lastInPlayOrder = cursor.getColumnCount();

            ContentValues[] contentValues = new ContentValues[mTrackIdsOverWhichToExecute.length];
            for(int i = 0; i < mTrackIdsOverWhichToExecute.length; i++) {
                contentValues[i] = new ContentValues();
                contentValues[i].put(AudioStorage.PlaylistMember.PLAY_ORDER, lastInPlayOrder + i);
                contentValues[i].put(AudioStorage.PlaylistMember.TRACK_ID, mTrackIdsOverWhichToExecute[i]);
            }

            int addedQuantity = resolver.bulkInsert(uri, contentValues);
            notifyUser(addedQuantity, mAvailablePlaylists.get(which).getName());
        }
    };

    private void notifyUser(int addedQuantity, String playlistName) {
        String ending = ((addedQuantity == 1) ? "" : "s");
        String message = mContext.getResources().getString(R.string.tracks_added_to_playlist, addedQuantity, ending, playlistName);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
