package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class ConfirmationAndDeletionDialog {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private AlertDialog mAlertDialog;

    public ConfirmationAndDeletionDialog(Context context) {
        mContext = context;
    }

    public void show() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        Resources resources = mContext.getResources();
        String message = resources.getString(R.string.delete_dialog_message);
        alertBuilder.setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(message)
                .setPositiveButton(R.string.delete_dialog_positive_button, mPositiveButtonListener)
                .setNegativeButton(R.string.delete_dialog_negative_button, mNegativeButtonListener);
        mAlertDialog = alertBuilder.create();
        mAlertDialog.show();
    }

    private DialogInterface.OnClickListener mPositiveButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "Delete clicked");
        }
    };

    private DialogInterface.OnClickListener mNegativeButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "Cancel clicked");
            mAlertDialog.dismiss();
        }
    };

}
