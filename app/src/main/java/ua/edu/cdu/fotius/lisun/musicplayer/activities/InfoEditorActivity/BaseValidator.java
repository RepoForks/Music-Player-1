package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.content.Context;

public abstract class BaseValidator {

    public static class ValidationResult {
        public boolean isSuccessful = true;
        public String invalidityMessage = null;

        public void clear() {
            isSuccessful = true;
            invalidityMessage = null;
        }
    }

    protected String mInvalidityMessage;

    protected BaseValidator(Context context) {
        mInvalidityMessage = context.getResources()
                .getString(getInvalidityMessageResourceId());
    }

    public abstract int getInvalidityMessageResourceId();
    public abstract void validate(String forValidation, ValidationResult validationResult);
}
