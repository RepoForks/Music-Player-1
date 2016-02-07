package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class StringToIntValidator extends BaseValidator{

    protected StringToIntValidator(Context context) {
        super(context);
    }

    @Override
    public int getInvalidityMessageResourceId() {
        return R.string.string_to_int_invalidity_message;
    }

    @Override
    public void validate(String forValidation, ValidationResult validationResult) {
        try {
            Integer.parseInt(forValidation);
        } catch (NumberFormatException nfe){
            validationResult.isSuccessful = false;
            validationResult.invalidityMessage = mInvalidityMessage;
        }
    }
}
