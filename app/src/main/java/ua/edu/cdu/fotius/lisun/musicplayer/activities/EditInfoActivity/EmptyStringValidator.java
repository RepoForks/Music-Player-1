package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class EmptyStringValidator extends BaseValidator{

    protected EmptyStringValidator(Context context) {
        super(context);
    }

    @Override
    public int getInvalidityMessageResourceId() {
        return R.string.empty_string_invalidity_message;
    }

    @Override
    public boolean validate(String forValidation) {
        if((forValidation == null) || (forValidation.isEmpty())) {
            return false;
        }
        return true;
    }
}
