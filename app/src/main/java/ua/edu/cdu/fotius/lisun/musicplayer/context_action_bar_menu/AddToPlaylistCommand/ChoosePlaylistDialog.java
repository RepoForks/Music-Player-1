package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu.AddToPlaylistCommand;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ChoosePlaylistDialog extends BaseDialog {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<PlaylistNameIdTuple> mAvailablePlaylists;

    public ChoosePlaylistDialog(Context context, long[] trackIds, AddToPlaylistResultListener listener) {
        super(context, trackIds, listener);
    }

    //TODO: maybe create base class
    public void show() {
        mAvailablePlaylists = getListItems();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setNegativeButton(R.string.dialog_negative_button, mOnNegativeButtonClick);
        dialogBuilder.setItems(getListItemNames(mAvailablePlaylists), mSpecificPlaylistClickListener);
        dialogBuilder.create().show();
    }

    private ArrayList<PlaylistNameIdTuple> getListItems() {
        ContentResolver resolver = mContext.getContentResolver();
        String[] cols = new String[] {
                AudioStorage.Playlist.PLAYLIST_ID,
                AudioStorage.Playlist.PLAYLIST
        };

        ArrayList<PlaylistNameIdTuple> playlists = new ArrayList<PlaylistNameIdTuple>();
        //first in the list is "New" item
        String createNewText = mContext.getResources().getString(R.string.create_new_playlist_text);
        playlists.add(new PlaylistNameIdTuple(-1, createNewText));

        Cursor cursor = resolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cols, null, null, null);
        if((cursor != null) && (cursor.moveToFirst())) {
            int idIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST_ID);
            int nameIdx = cursor.getColumnIndexOrThrow(AudioStorage.Playlist.PLAYLIST);
            while(!cursor.isAfterLast()) {
                playlists.add(new PlaylistNameIdTuple(cursor.getLong(idIdx),
                        cursor.getString(nameIdx)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return playlists;
    }

    private String[] getListItemNames(ArrayList<PlaylistNameIdTuple> playlists) {
        String[] playlistNames = new String[playlists.size()];
        for(int i = 0; i < playlists.size(); i++) {
            playlistNames[i] = playlists.get(i).getName();
        }
        return playlistNames;
    }

    private DialogInterface.OnClickListener mOnNegativeButtonClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener mSpecificPlaylistClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == 0) { //create new playlist item clicked
                new CreateNewPlaylistDialog(mContext, mTrackIds, mListener).show();
            } else {
                long playlistId = mAvailablePlaylists.get(which).getId();
                int addedQuantity = DatabaseUtils.addToPlaylist(mContext, playlistId, mTrackIds);
                mListener.addedToPlaylistUserNotification(addedQuantity, mAvailablePlaylists.get(which).getName());
            }
        }
    };


}
