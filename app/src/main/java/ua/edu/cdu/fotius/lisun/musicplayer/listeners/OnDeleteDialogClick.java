package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

public class OnDeleteDialogClick implements View.OnClickListener{
    private AsyncTask mAsyncTask;

    public OnDeleteDialogClick(DialogFragment fragment, AsyncTask asyncTask) {
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(View v) {
        mAsyncTask.execute();
    }
}
