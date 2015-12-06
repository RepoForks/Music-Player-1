package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;


import android.content.Context;

import java.util.ArrayList;

public class YearValidatorSetCreator extends BaseValidatorsSetCreator{

    public YearValidatorSetCreator(Context context) {
        super(context);
    }

    @Override
    public ArrayList<BaseValidator> create() {
        ArrayList<BaseValidator> validators = new ArrayList<BaseValidator>();
        validators.add(new EmptyStringValidator(mContext));
        validators.add(new StringToIntValidator(mContext));
        return validators;
    }
}
