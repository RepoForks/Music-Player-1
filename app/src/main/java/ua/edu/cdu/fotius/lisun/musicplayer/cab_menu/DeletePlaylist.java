package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeletePlaylistsAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class DeletePlaylist extends Command{

    private final String TAG = getClass().getSimpleName();

    public DeletePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        DeletePlaylistsAsyncTask deleteTask =
                new DeletePlaylistsAsyncTask(mFragment, ids);
        Resources resources = mFragment.getActivity().getResources();

        String changeablePart = ((ids.length > 1) ?
                resources.getString(R.string.delete_dialog_playlists_prompt) :
                resources.getString(R.string.delete_dialog_playlist_prompt));
        String message =
                resources.getString(R.string.delete_playlist_dialog_message, ids.length, changeablePart);

        DeleteDialog.Builder builder = new DeleteDialog.Builder(mFragment.getContext());
        builder.setAsyncTask(deleteTask);
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message);
        builder.create().show();
    }
}
