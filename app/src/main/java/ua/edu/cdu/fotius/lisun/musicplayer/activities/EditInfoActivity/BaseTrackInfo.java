package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import java.util.ArrayList;

public class BaseTrackInfo {
    private String mData;
    private ArrayList<BaseValidator> mValidators;
    private String mContentProviderColumnName;
    private boolean mIsChanged;
    private String mInvalidityMessage;
    private String mInputFieldTitle;

    public BaseTrackInfo(String title, String data, String contentProviderColumnName) {
        mInputFieldTitle = title;
        mData = data;
        mContentProviderColumnName = contentProviderColumnName;
    }

    public void setValidators(ArrayList<BaseValidator> validators) {
        mValidators = validators;
    }

    /*returns false if data don't pass the validation*/
    public boolean setData(String newData) {
        if(!validate(newData)) {
            return false;
        }

        if(!newData.equals(mData)) {
            mData = newData;
            mIsChanged = true;
        }
        return true;
    }

    public String getData() {
        return mData;
    }

    public String getContentProviderColumnName() {
        return mContentProviderColumnName;
    }

    public String getInvalidityMessage() {
        return mInvalidityMessage;
    }

    public String getInputFieldTitle() {
        return mInputFieldTitle;
    }

    public boolean isChanged() {
        return mIsChanged;
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
