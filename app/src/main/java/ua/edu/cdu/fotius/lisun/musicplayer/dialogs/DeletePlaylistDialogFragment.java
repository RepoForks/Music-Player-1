package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeletePlaylistsAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogDeleteClick;

public class DeletePlaylistDialogFragment extends BaseDeleteDialogFragment {
    @Override
    protected String getTitle() {
        return getString(R.string.delete_playlist_dialog_title);
    }

    @Override
    protected String getMessage() {
        String changeablePart = ((mIds.length > 1) ?
                getString(R.string.delete_dialog_playlists_prompt) :
                getString(R.string.delete_dialog_playlist_prompt));
        return getString(R.string.delete_playlist_dialog_message, mIds.length, changeablePart);
    }

    @Override
    protected View.OnClickListener getPositiveButtonListener() {
        DeletePlaylistsAsyncTask deleteTask =
                new DeletePlaylistsAsyncTask(this, mIds);
        return new OnDialogDeleteClick(deleteTask);
    }
}
