package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.BaseValidator;

public class EditTextWithValidation extends EditText {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<BaseValidator> mValidators;
    private String mInitiallyInputedData;

    public EditTextWithValidation(Context context) {
        super(context);
    }

    public EditTextWithValidation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithValidation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInitialText(String s) {
        mInitiallyInputedData = s;
        setText(s);
    }

    public void validateInput(BaseValidator.ValidationResult validationResult) {
        validate(getText().toString(), validationResult);
    }

    public boolean isChanged() {
        if(mInitiallyInputedData == null) {
            throw new IllegalStateException("Should call setInitialText(String s) first");
        }
        return !mInitiallyInputedData.equals(getText().toString());
    }

    public void setValidators(ArrayList<BaseValidator> validators) {
        mValidators = validators;
    }

    private void validate(String forValidation, BaseValidator.ValidationResult validationResult) {
        for (BaseValidator validator : mValidators) {
            validator.validate(forValidation, validationResult);
            if (!validationResult.mIsSuccessful) return;
        }
    }
}