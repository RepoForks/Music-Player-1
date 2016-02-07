package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.activities.InfoEditorActivity.BaseValidator;

public class EditTextWithValidation extends EditText {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<BaseValidator> mValidators;
    private String mInitialText;

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
        mInitialText = s;
        setText(s);
    }

    /**
     * @param validationResult - after validation includes
     *                         validation result and error message if invalid;
     * @return - valid input or null(if invalid)
     */
    public String validateInput(BaseValidator.ValidationResult validationResult) {
        String trimedInput = getText().toString().trim();
        Log.d(TAG, "Trimed string-->" + trimedInput + "<--");
        return validate(trimedInput, validationResult);
    }

    public boolean isChanged() {
        if(mInitialText == null) {
            throw new IllegalStateException("Should call setInitialText(String s) first");
        }
        return !mInitialText.equals(getText().toString());
    }

    public void setValidators(ArrayList<BaseValidator> validators) {
        mValidators = validators;
    }

    private String validate(String forValidation, BaseValidator.ValidationResult validationResult) {
        for (BaseValidator validator : mValidators) {
            validator.validate(forValidation, validationResult);
            if (!validationResult.isSuccessful) return null;
        }
        return forValidation;
    }

    public String getInitialText() {
        return mInitialText;
    }
}
