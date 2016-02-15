package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
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
            //if value hasn't been set
            //any value(even empty string) is change
            return true;
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

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mInitialText = mInitialText;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mInitialText = ss.mInitialText;
    }

    static class SavedState extends BaseSavedState {
        String mInitialText;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mInitialText = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(mInitialText);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
