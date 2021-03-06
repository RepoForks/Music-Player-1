package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.cab_menu.PlaylistNameValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class OnRenameDialogPositiveClick extends OnSingleInputDialogPositiveClick {

    public OnRenameDialogPositiveClick(Context context, AsyncTask asyncTask,
                                       BaseValidatorsSetCreator validatorsSet) {
        super(context, asyncTask, validatorsSet);
    }

    @Override
    public void doOnSuccessValidation(EditTextWithValidation inputField, String newValue) {
        mAsyncTask.execute(inputField.getInitialText(), newValue);
    }

    @Override
    public void doOnValueIsNotChanged() {
        Toast.makeText(mContext,
                mContext.getResources().getString(R.string.rename_playlist_with_same_name),
                Toast.LENGTH_SHORT).show();
    }
}
