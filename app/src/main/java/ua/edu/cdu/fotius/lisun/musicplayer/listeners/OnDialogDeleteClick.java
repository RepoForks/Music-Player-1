package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class OnDialogDeleteClick implements View.OnClickListener{
    private AsyncTask mAsyncTask;

    public OnDialogDeleteClick(AsyncTask asyncTask) {
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(View v) {
        mAsyncTask.execute();
    }
}
