package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class DeleteDialog {
    public static class Builder extends AlertDialog.Builder {

        private AsyncTask mAsyncTask;

        public Builder(Context context) {
            super(context);
        }

        public Builder(Context context, int theme) {
            super(context, theme);
        }

        public void setAsyncTask(AsyncTask deletionAsyncTask) {
            mAsyncTask = deletionAsyncTask;
        }

        @Override
        public AlertDialog create() {
            setPositiveButton(R.string.delete_dialog_positive_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAsyncTask.execute();
                }
            });

            setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return super.create();
        }
    }
}
