package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeleteTracksAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;


public class Delete extends Command {

    public Delete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        createDialog(ids).show();
    }

    public AlertDialog createDialog(long[] ids) {
        Resources resources = mFragment.getActivity().getResources();
        String changeablePart = ((ids.length > 1) ?
                resources.getString(R.string.delete_dialog_tracks_prompt) :
                resources.getString(R.string.delete_dialog_track_prompt));
        String message = resources.getString(R.string.delete_dialog_message, ids.length, changeablePart);

        AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getContext());
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message);
        builder.setNegativeButton(R.string.dialog_negative_button, new OnDialogNegativeClick());
        DeleteTracksAsyncTask deleteTask =
                new DeleteTracksAsyncTask(mFragment, mServiceWrapper, ids);
        builder.setPositiveButton(R.string.delete_dialog_positive_button, new OnDeleteDialogClick(deleteTask));
        return builder.create();
    }
}
