package ua.edu.cdu.fotius.lisun.musicplayer.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.DeleteTracksAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDeleteDialogClick;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogNegativeClick;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class DeleteTrackDialogFragment extends BaseDeleteDialogFragment {
    private MediaPlaybackServiceWrapper mServiceWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.delete_dialog_title);
    }

    @Override
    protected String getMessage() {
        String changeablePart = ((mIds.length > 1) ?
                getString(R.string.delete_dialog_tracks_prompt) :
                getString(R.string.delete_dialog_track_prompt));
        return getString(R.string.delete_dialog_message, mIds.length, changeablePart);
    }

    @Override
    protected View.OnClickListener getPositiveButtonListener() {
        DeleteTracksAsyncTask deleteTask =
                new DeleteTracksAsyncTask(this, mServiceWrapper, mIds);
        return new OnDeleteDialogClick(this, deleteTask);
    }
}
