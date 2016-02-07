package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeletePlaylistsAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeleteTracksAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class DeletePlaylist extends Command{

    private final String TAG = getClass().getSimpleName();

    public DeletePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        createDialog(ids).show();
    }

    public AlertDialog createDialog(long[] ids) {
        Resources resources = mFragment.getActivity().getResources();
        String changeablePart = ((ids.length > 1) ?
                resources.getString(R.string.delete_dialog_playlists_prompt) :
                resources.getString(R.string.delete_dialog_playlist_prompt));
        String message =
                resources.getString(R.string.delete_playlist_dialog_message, ids.length, changeablePart);

        AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getContext());
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message);
        builder.setNegativeButton(R.string.dialog_negative_button, new OnDialogNegativeClick());
        DeletePlaylistsAsyncTask deleteTask =
                new DeletePlaylistsAsyncTask(mFragment, ids);
        builder.setPositiveButton(R.string.delete_dialog_positive_button, new OnDeleteDialogClick(deleteTask));
        return builder.create();
    }
}
