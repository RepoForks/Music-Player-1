package ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity;

import android.content.Context;

public abstract class BaseValidator {

    public static class ValidationResult {
        public boolean mIsSuccessful = true;
        public String mInvalidityMessage = null;

        public void clear() {
            mIsSuccessful = true;
            mInvalidityMessage = null;
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
