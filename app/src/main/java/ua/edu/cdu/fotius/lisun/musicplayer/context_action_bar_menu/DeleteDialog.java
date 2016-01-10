package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.io.File;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class DeleteDialog extends BaseDialog{

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public DeleteDialog(Fragment fragment, long[] trackIds, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, trackIds);
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void show() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mFragment.getActivity());
        Resources resources = mFragment.getActivity().getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        alertBuilder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message)
                .setPositiveButton(R.string.delete_dialog_positive_button, mPositiveButtonListener)
                .setNegativeButton(R.string.dialog_negative_button, mNegativeButtonListener);
        alertBuilder.create().show();
    }

    private DialogInterface.OnClickListener mPositiveButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DeleteAsyncTask task = new DeleteAsyncTask(mFragment, mServiceWrapper, mTrackIds);
            task.execute();
        }
    };

    private DialogInterface.OnClickListener mNegativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
}
