package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import java.util.ArrayList;

public class InfoElement {

    private String mData;
    private ArrayList<BaseValidator> mValidators;
    private String mContentProviderColumnName;
    private boolean mIsChanged;
    private String mInputFieldTitle;

    public InfoElement(String title, String data, String contentProviderColumnName) {
        mInputFieldTitle = title;
        mData = data;
        mContentProviderColumnName = contentProviderColumnName;
    }

    public void setValidators(ArrayList<BaseValidator> validators) {
        mValidators = validators;
    }

    /*returns if data don't pass the validation*/
    public void setData(String newData, BaseValidator.ValidationResult validationResult) {
        validate(newData, validationResult);
        if(!validationResult.mIsSuccessful) {
            return;
        }

        if(!newData.equals(mData)) {
            mData = newData;
            mIsChanged = true;
        }
    }

    public String getData() {
        return mData;
    }

    public String getContentProviderColumnName() {
        return mContentProviderColumnName;
    }

    public String getInputFieldTitle() {
        return mInputFieldTitle;
    }

    public boolean isChanged() {
        return mIsChanged;
    }

    private void validate(String forValidation, BaseValidator.ValidationResult validationResult){
        validationResult.mFieldTitle = mInputFieldTitle;
        for(BaseValidator validator : mValidators) {
            validator.validate(forValidation, validationResult);
            if(!validationResult.mIsSuccessful) return;
        }
    }
}
