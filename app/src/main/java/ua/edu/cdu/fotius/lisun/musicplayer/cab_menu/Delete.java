package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeleteTracksAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;


public class Delete extends Command {

    public Delete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        DeleteDialog.Builder builder = new DeleteDialog.Builder(mFragment.getContext());

        DeleteTracksAsyncTask deleteTask =
                new DeleteTracksAsyncTask(mFragment, mServiceWrapper, idsOverWhichToExecute);
        builder.setAsyncTask(deleteTask);

        Resources resources = mFragment.getActivity().getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message);
        builder.create().show();
    }
}
