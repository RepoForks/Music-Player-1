package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.Context;

public abstract class BaseValidator {

    private String mInvalidityMessage;

    protected BaseValidator(Context context) {
        mInvalidityMessage = context.getResources()
                .getString(getInvalidityMessageResourceId());
    }

    public String getInvalidityMessage() {
        return mInvalidityMessage;
    }

    public abstract int getInvalidityMessageResourceId();
    public abstract boolean validate(String forValidation);
}
