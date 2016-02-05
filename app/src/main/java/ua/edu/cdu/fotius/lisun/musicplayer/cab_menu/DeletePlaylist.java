package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeletePlaylistsAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class DeletePlaylist extends Command{

    private final String TAG = getClass().getSimpleName();

    public DeletePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {

        Log.d(TAG, "Delete playlist");

        DeleteDialog.Builder builder = new DeleteDialog.Builder(mFragment.getContext());

        DeletePlaylistsAsyncTask deleteTask =
                new DeletePlaylistsAsyncTask(mFragment, idsOverWhichToExecute);
        builder.setAsyncTask(deleteTask);

        Resources resources = mFragment.getActivity().getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message);
        builder.create().show();
    }
}
