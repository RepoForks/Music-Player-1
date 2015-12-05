package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

public abstract class BaseValidator {

    class VelidationResult {
        public boolean mIsValid;
        public String mInvalidityMessage;
    }

    private String mInvalidityMessage;

    protected BaseValidator(String invalidityMessage) {
        mInvalidityMessage = invalidityMessage;
    }

    public String getInvalidityMessage() {
        return mInvalidityMessage;
    }

    public abstract boolean validate(Object forValidation);
}
