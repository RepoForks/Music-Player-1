package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.EditText;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class OnRenameDialogPositiveClick implements DialogInterface.OnClickListener {

    private AsyncTask mAsyncTask;

    public OnRenameDialogPositiveClick(AsyncTask asyncTask) {
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        Dialog dialog = (Dialog) dialogInterface;
        EditText inputField = (EditText) dialog.findViewById(R.id.playlist_name);
        String newName = inputField.getText().toString();
        mAsyncTask.execute();
    }
}
