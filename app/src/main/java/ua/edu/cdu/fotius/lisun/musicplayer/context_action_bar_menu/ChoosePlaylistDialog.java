package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.AddToPlaylistAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.AsyncFragmentWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ChoosePlaylistDialog extends BaseDialog {

    private ArrayList<PlaylistNameIdTuple> mAvailablePlaylists;

    public ChoosePlaylistDialog(Fragment fragment, ArrayList<PlaylistNameIdTuple> playlistsInfo, long[] trackIds) {
        super(fragment, trackIds);
        mAvailablePlaylists = playlistsInfo;
    }

    @Override
    public void show() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mFragment.getActivity());
        dialogBuilder.setNegativeButton(R.string.dialog_negative_button, mOnNegativeButtonClick);
        dialogBuilder.setItems(getListItemNames(mAvailablePlaylists), mSpecificPlaylistClickListener);
        dialogBuilder.create().show();
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
                new CreateNewPlaylistDialog(mFragment, mTrackIds).show();
            } else {
                AddToPlaylistAsyncTask addTask =
                        new AddToPlaylistAsyncTask(mFragment, mAvailablePlaylists.get(which), mTrackIds);
                addTask.execute();
            }
        }
    };


}
