package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncUpdater;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public class OnRenameDialogPositiveClick implements DialogInterface.OnClickListener {

    private Fragment mFragment;
    private long mId;

    public OnRenameDialogPositiveClick(Fragment fragment, long playlistId) {
        mFragment = fragment;
        mId = playlistId;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        Dialog dialog = (Dialog) dialogInterface;
        EditTextWithValidation inputField =
                (EditTextWithValidation) dialog.findViewById(R.id.playlist_name);
        String newName = null;
        if(inputField.isChanged()) {
            inputField.setValidators(new PlaylistNameValidatorsSetCreator(mFragment.getContext()).create());
            BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
            newName = inputField.validateInput(validationResult);
            if(!validationResult.isSuccessful) {
                Toast.makeText(mFragment.getContext(),
                        validationResult.invalidityMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            new PlaylistNameAsyncUpdater(mFragment, mId, inputField.getInitialText(), newName)
                    .execute();
        }
    }
}
