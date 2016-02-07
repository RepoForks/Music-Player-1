package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.content.DialogInterface;
import android.os.AsyncTask;

public class OnDeleteDialogClick implements DialogInterface.OnClickListener{

    private AsyncTask mAsyncTask;

    public OnDeleteDialogClick(AsyncTask asyncTask) {
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mAsyncTask.execute();
    }
}
