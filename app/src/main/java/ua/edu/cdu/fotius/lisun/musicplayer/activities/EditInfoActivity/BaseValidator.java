package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.Context;

public abstract class BaseValidator {

    static class ValidationResult {
        public boolean mIsSuccessful = true;
        public String mInvalidityMessage = null;
        public String mFieldTitle = null;

        public void clear() {
            mIsSuccessful = true;
            mInvalidityMessage = null;
            mFieldTitle = null;
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
