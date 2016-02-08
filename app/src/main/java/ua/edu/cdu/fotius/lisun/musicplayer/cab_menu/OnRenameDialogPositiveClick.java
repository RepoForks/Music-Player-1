package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncUpdater;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.RenamePlaylistDialog;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class OnRenameDialogPositiveClick implements View.OnClickListener {

    private Context mContext;
    private AsyncTask mAsyncTask;

    public OnRenameDialogPositiveClick(Context context, AsyncTask asyncTask) {
        mContext = context;
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(View v) {
        ViewGroup parent = (ViewGroup) v.getRootView();
        EditTextWithValidation inputField =
                (EditTextWithValidation) parent.findViewById(R.id.dialog_input);
        String newName = null;
        if (inputField.isChanged()) {
            inputField.setValidators(new PlaylistNameValidatorsSetCreator(mContext).create());
            BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
            newName = inputField.validateInput(validationResult);
            if (!validationResult.isSuccessful) {
                Toast.makeText(mContext,
                        validationResult.invalidityMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            mAsyncTask.execute(inputField.getInitialText(), newName);
        } else {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.rename_playlist_with_same_name),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
