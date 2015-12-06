package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import android.content.Context;

import java.util.ArrayList;

public class TitleValidatorsSetCreator extends BaseValidatorsSetCreator{

    public TitleValidatorsSetCreator(Context context) {
        super(context);
    }

    @Override
    public ArrayList<BaseValidator> create() {
        ArrayList<BaseValidator> validators = new ArrayList<BaseValidator>();
        //TODO: add invalidity message
        validators.add(new EmptyStringValidator(mContext));
        return validators;
    }
}
