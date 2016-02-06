package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class EmptyStringValidator extends BaseValidator{

    public EmptyStringValidator(Context context) {
        super(context);
    }

    @Override
    public int getInvalidityMessageResourceId() {
        return R.string.empty_string_invalidity_message;
    }

    @Override
    public void validate(String forValidation, ValidationResult validationResult) {
        if((forValidation == null) || (forValidation.isEmpty())) {
            validationResult.mIsSuccessful = false;
            validationResult.mInvalidityMessage = mInvalidityMessage;
        }
    }
}
