package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class Delete extends Command{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<Long> mTrackIdsOverWhichToExecute;

    public Delete(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(serviceWrapper);
        mContext = context;
    }

    @Override
    public void execute(ArrayList<Long> idsOverWhichToExecute) {
        mTrackIdsOverWhichToExecute = idsOverWhichToExecute;
        showConfirmationDialog();
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        Resources resources = mContext.getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        alertBuilder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message)
                .setPositiveButton(R.string.delete_dialog_positive_button, mPositiveButtonListener)
                .setNegativeButton(R.string.delete_dialog_negative_button, mNegativeButtonListener);
        alertBuilder.create().show();
    }

    private DialogInterface.OnClickListener mPositiveButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "Delete clicked");
            for(Long trackId : mTrackIdsOverWhichToExecute) {
                Log.d(TAG, "id: " + trackId);
            }
        }
    };

    public void deleteFromCurrentPlaylist() {

    }

    private DialogInterface.OnClickListener mNegativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "Cancel clicked");
            dialog.dismiss();
        }
    };
}
