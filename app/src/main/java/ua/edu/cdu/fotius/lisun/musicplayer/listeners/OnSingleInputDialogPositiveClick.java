package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidatorsSetCreator;
import ua.edu.cdu.fotius.lisun.musicplayer.views.EditTextWithValidation;

public abstract class OnSingleInputDialogPositiveClick implements View.OnClickListener{

    protected Context mContext;
    protected AsyncTask mAsyncTask;
    protected BaseValidatorsSetCreator mValidatorsSet;

    public OnSingleInputDialogPositiveClick(Context context, AsyncTask asyncTask,
                                            BaseValidatorsSetCreator validatorsSet) {
        mContext = context;
        mAsyncTask = asyncTask;
        mValidatorsSet = validatorsSet;
    }

    @Override
    public void onClick(View v) {
        ViewGroup parent = (ViewGroup) v.getRootView();
        EditTextWithValidation inputField =
                (EditTextWithValidation) parent.findViewById(R.id.dialog_input);
        String newValue = null;
        if (inputField.isChanged()) {
            inputField.setValidators(mValidatorsSet.create());
            BaseValidator.ValidationResult validationResult = new BaseValidator.ValidationResult();
            newValue = inputField.validateInput(validationResult);
            if (!validationResult.isSuccessful) {
                Toast.makeText(mContext,
                        validationResult.invalidityMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            doOnSuccessValidation(inputField, newValue);
        } else {
           doOnValueIsNotChanged();
        }
    }

    public abstract void doOnSuccessValidation(EditTextWithValidation inputField, String newValue);
    public abstract void doOnValueIsNotChanged();
}
