package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import java.util.ArrayList;

public class BaseTrackInfo {
    private String mData;
    private ArrayList<BaseValidator> mValidators;
    private String mContentProviderColumnName;
    private boolean mIsChanged;
    private String mInvalidityMessage;

    public BaseTrackInfo(String data) {
        mData = data;
    }

    public void setValidators(ArrayList<BaseValidator> validators) {
        mValidators = validators;
    }

    public boolean setData(String newData) {
        if(!validate(newData)) {
            //TODO: return false
        }

        //TODO: if(has been changed) mIsChanged = true
        //setValue
    }

    public String getData() {
        return mData;
    }

    public String getInvalidityMessage() {
        return mInvalidityMessage;
    }

    private boolean validate(String forValidation){
        for(BaseValidator validator : mValidators) {
            boolean isValid = validator.validate(forValidation);
            if(!isValid) {
                mInvalidityMessage = validator.getInvalidityMessage();
                return false;
            }
        }
        return true;
    }
}
